from dataclasses import dataclass, field, replace


@dataclass
class Spell:
    cost: int

    def apply(self, state):
        return replace(
            state,
            player_mana=state.player_mana - self.cost,
            mana_spent=state.mana_spent + self.cost,
        )


@dataclass
class Effect:
    time_left: int


@dataclass(kw_only=True)
class State:
    player_hp: int
    player_armour: int
    player_mana: int
    boss_hp: int
    boss_damage: int
    effects: list[Effect] = field(default_factory=list)

    mana_spent: int = 0
    has_player_won: bool | None = None


@dataclass
class MagicMissile(Spell):
    cost: int = field(default=53)

    def apply(self, state):
        return super().apply(replace(state, boss_hp=state.boss_hp - 4))


@dataclass
class Drain(Spell):
    cost: int = field(default=73)

    def apply(self, state):
        return super().apply(
            replace(state, boss_hp=state.boss_hp - 2, player_hp=state.player_hp + 2)
        )


@dataclass
class ShieldEffect(Effect):
    def apply(self, state):
        return replace(state, player_armour=state.player_armour + 7)


@dataclass
class Shield(Spell):
    cost: int = field(default=113)

    def apply(self, state):
        return super().apply(
            replace(state, effects=state.effects + [ShieldEffect(time_left=6)])
        )


@dataclass
class PoisonEffect(Effect):
    def apply(self, state):
        return replace(state, boss_hp=state.boss_hp - 3)


@dataclass
class Poison(Spell):
    cost: int = field(default=173)

    def apply(self, state):
        return super().apply(
            replace(state, effects=state.effects + [PoisonEffect(time_left=6)])
        )


@dataclass
class RechargeEffect(Effect):
    def apply(self, state):
        return replace(state, player_mana=state.player_mana + 101)


@dataclass
class Recharge(Spell):
    cost: int = field(default=229)

    def apply(self, state):
        return super().apply(
            replace(state, effects=state.effects + [RechargeEffect(time_left=5)])
        )


def apply_effects(state):
    next_state = replace(state)
    next_effects = []
    for effect in state.effects:
        if effect.time_left == 0:
            continue
        next_state = effect.apply(next_state)
        next_effects.append(replace(effect, time_left=effect.time_left - 1))
    return replace(next_state, effects=next_effects)


def neighbours(state: State):
    return [
        spell
        for spell in [MagicMissile, Drain, Shield, Poison, Recharge]
        if spell.cost <= state.player_mana
    ]


def tick(state: State):
    next_state = replace(state)

    # player's turn
    next_state = apply_effects(next_state)
    for spell in [MagicMissile, Drain, Shield, Poison, Recharge]:
        if spell.cost <= state.player_mana:
            next_state = spell().apply(state)

    if next_state.boss_hp <= 0:
        next_state.has_player_won = True
        return next_state

    # boss's turn
    next_state = apply_effects(next_state)
    next_state.player_hp -= max(1, state.boss_damage - state.player_armour)

    return next_state


start = State(player_hp=50, player_armour=0, player_mana=500, boss_hp=55, boss_damage=8)
print(start)
print(tick(start))
