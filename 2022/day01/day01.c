#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int cmpfunc(const void *a, const void *b)
{
  return (*(int *)b - *(int *)a);
}

int main(void)
{
  char *line = NULL;
  size_t len = 0;
  int sum = 0;
  int sums[999];
  int n = 0;

  while (getline(&line, &len, stdin) != -1)
  {
    if (strcmp(line, "\n") == 0)
    {
      sums[n++] = sum;
      sum = 0;
    }
    else
    {
      sum += atoi(line);
    }
  }
  sums[n++] = sum;

  qsort(sums, n, sizeof(int), cmpfunc);
  printf("Part 1: %d\n", sums[0]);
  printf("Part 2: %d\n", sums[0] + sums[1] + sums[2]);

  if (line)
    free(line);
  exit(EXIT_SUCCESS);
}
