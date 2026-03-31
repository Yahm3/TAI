import std/[os, strutils]

proc readByChar(filepath: string): string =
  var finalString: string
  if not fileExists(filepath):
    return "[INFO]: File not found."

  var f: File
  var offset = 0

  if open(f, filepath, fmRead):
    try:
      while not endOfFile(f):
        finalString.add(toHex(offset, 8).toLowerAscii() & ": ")

        for i in 0 ..< 16:
          if not endOfFile(f):
            let hexVal = toHex(byte(readChar(f)), 2).toLowerAscii()
            finalString.add(hexVal)

            if (i + 1) mod 2 == 0:
              finalString.add(" ")
          else:
            finalString.add("  ")
            if (i + 1) mod 2 == 0: finalString.add(" ")

        finalString.add("\n")
        offset += 16

    finally:
      close(f)
    return finalString
  else:
    return "[ERROR]: Could not open file."

when isMainModule:
  if paramCount() > 0:
    let targetFile = paramStr(1)
    echo readByChar(targetFile)
  else:
    echo "Usage: ./main <filepath>"
