recipes = "37"
first_elf = 0
second_elf = 1

n = 165061
for i in range(n * 100):
    first_recipe = int(recipes[first_elf])
    second_recipe = int(recipes[second_elf])
    recipes += str(first_recipe + second_recipe)
    first_elf = (first_elf + 1 + first_recipe) % len(recipes)
    second_elf = (second_elf + 1 + second_recipe) % len(recipes)

print(recipes[n:n + 10])
print(recipes.index("165061"))
