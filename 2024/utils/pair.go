package utils

type Pair struct {
	R int
	C int
}

func (p1 Pair) Plus(p2 Pair) Pair {
	return Pair{p1.R + p2.R, p1.C + p2.C}
}

func (p1 Pair) Minus(p2 Pair) Pair {
	return Pair{p1.R - p2.R, p1.C - p2.C}
}

func (p Pair) Times(n int) Pair {
	return Pair{p.R * n, p.C * n}
}

func (p Pair) Divide(n int) Pair {
	return Pair{p.R / n, p.C / n}
}

func (p1 Pair) Mod(p2 Pair) Pair {
	return Pair{p1.R % p2.R, p1.C % p2.C}
}

func (p Pair) TurnRight() Pair {
	return Pair{p.C, -p.R}
}
