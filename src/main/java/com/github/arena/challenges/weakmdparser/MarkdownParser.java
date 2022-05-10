package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    public MarkdownParser() {
    }

    public String parse(String markdown) {
        String[] lines = markdown.split("\n");
        String result = "";
        boolean activeList = false;

        for (int i = 0; i < lines.length; i++) {

            String theLine = parseHeader(lines[i]);

            if (theLine == null) {
                theLine = parseListItem(lines[i]);
            }

            if (theLine == null) {
                theLine = parseParagraph(lines[i]);
            }

            if (theLine.matches("(<li>).*") && !theLine.matches("(<h).*") && !theLine.matches("(<p>).*") && !activeList) {
                activeList = true;
                result = result + "<ul>";
                result = result + theLine;
            } else if (!theLine.matches("(<li>).*") && activeList) {
                activeList = false;
                result = result + "</ul>";
                result = result + theLine;
            } else {
                result = result + theLine;
            }
        }

        if (activeList) {
            result = result + "</ul>";
        }

        return result;
    }

    private String parseHeader(String markdown) {
        int count = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            count++;
        }

        if (count == 0) {
            return null;
        }

        return "<h" + Integer.toString(count) + ">" + markdown.substring(count + 1) + "</h" + Integer.toString(count) + ">";
    }

    private String parseListItem(String markdown) {
        if (markdown.startsWith("*")) {
            String skipAsterisk = markdown.substring(2);
            String listItemString = parseInnerContent(skipAsterisk);
            return "<li>" + listItemString + "</li>";
        }

        return null;
    }

    private String parseParagraph(String markdown) {
        return "<p>" + parseInnerContent(markdown) + "</p>";
    }

    private String parseInnerContent(String markdown) {
        String boldParsed = parseBold(markdown);
        return parseItalic(boldParsed);
    }

    private String parseItalic(String markdown){
        //Um die Methode vor parseBold() verwenden zu können, müsste das Erkennen des TeilStrings verändert werden, z.B. per RegExp
        String lookingFor = "_(.+)_";
        String update = "<em>$1</em>";
        return markdown.replaceAll(lookingFor, update);
    }

    private String parseBold(String markdown){
        String lookingFor = "__(.+)__";
        String update = "<strong>$1</strong>";
        return markdown.replaceAll(lookingFor, update);
    }
}
