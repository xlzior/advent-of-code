export async function readLines() {
  const filename = Bun.argv[2];
  if (!filename) throw new Error("no filename given");

  const text = await Bun.file(filename).text();
  return text.trim().split("\n");
}
