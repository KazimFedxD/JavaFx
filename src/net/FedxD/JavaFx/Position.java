package net.FedxD.JavaFx;

public class Position {
    int idx;
    int ln;
    int col;
    String fileName;
    String fileText;


    public Position(int index, int line, int col, String fileName, String fileText) {
        this.idx = index;
        this.ln = line;
        this.col = col;
        this.fileName = fileName;
        this.fileText = fileText;
    }

    public Position advance() {
        idx++;
        col++;
        return this;
    }

    public Position advance(char currentChar) {
        idx++;
        col++;

        if (currentChar == '\n') {
            ln++;
            col = 0;
        }

        return this;
    }

    public Position copy() {
        return new Position(idx, ln, col, fileName, fileText);
    }

    public static String stringWithArrows(String text, Position posStart, Position posEnd) {
        StringBuilder result = new StringBuilder();

        // Calculate indices
        int idxStart = text.lastIndexOf("\n", posStart.idx);
        if (idxStart < 0) {
            idxStart = 0;
        }
        int idxEnd = text.indexOf("\n", idxStart + 1);
        if (idxEnd < 0) {
            idxEnd = text.length();
        }

        // Determine the number of lines covered by the range.
        int lineCount = posEnd.ln - posStart.ln + 1;
        for (int i = 0; i < lineCount; i++) {
            // Extract the current line
            String line = text.substring(idxStart, idxEnd);
            // Determine the starting and ending column for the arrows
            int colStart = (i == 0) ? posStart.col : 0;
            int colEnd = (i == lineCount - 1) ? posEnd.col : line.length() - 1;

            // Append the line and a newline
            result.append(line).append("\n");
            // Append the arrow line: spaces followed by carets (^)
            result.append(" ".repeat(Math.max(0, colStart)))
                    .append("^".repeat(Math.max(0, colEnd - colStart)));

            // Recalculate indices for the next line
            idxStart = idxEnd;
            idxEnd = text.indexOf("\n", idxStart + 1);
            if (idxEnd < 0) {
                idxEnd = text.length();
            }
        }

        // Remove any tab characters
        return result.toString().replace("\t", "");
    }
}
