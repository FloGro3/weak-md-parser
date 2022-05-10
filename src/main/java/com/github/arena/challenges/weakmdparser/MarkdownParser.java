package com.github.arena.challenges.weakmdparser;

import java.util.ArrayList;
import java.util.List;

public class MarkdownParser {

    public MarkdownParser() {
    }

    public String parse(String markdown) {
        String[] lines = markdown.split("\n");
        String result = "";
        List<String> listItems = new ArrayList<String>();

        for (int i = 0; i < lines.length; i++) {

            if (isListItem(lines[i])){
                listItems.add(lines[i]);
                if(i+1 == lines.length){
                    result += parseList(listItems);
                }
                continue;
            }else if (!listItems.isEmpty()){
                result += parseList(listItems);
                listItems.clear();
            }

            if(isHeader(lines[i])){
                result += parseHeader(lines[i]);
                continue;
            }

            result += parseParagraph(lines[i]);
        }

        result = parseInnerContent(result);

        return result;
    }

    private String parseHeader(String markdown) {
        int count = 0;

        for (int i = 0; i < markdown.length() && markdown.charAt(i) == '#'; i++) {
            count++;
        }

        if (count == 0) {
            return null;
        } else {
            return "<h" + count + ">" + markdown.substring(count + 1) + "</h" + count + ">";
        }
    }

    private String parseListItem(String markdown) {
        return "<li>" + markdown.substring(2) + "</li>";
    }

    private String parseParagraph(String markdown) {
        return "<p>" + markdown + "</p>";
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

    private String parseList(List<String> listItems){
        //Für viele aufeinander folgende ListItems besser einen StringBuilder oder StringBuffer für bessere Perfomance einsetzen
        String result = "";
        for (String listItem:listItems){
            result += parseListItem(listItem);
        }
        return "<ul>" + result + "</ul>";
    }

    private boolean isListItem(String markdown){
        if (markdown.startsWith("*")){
            return true;
        } else {
            return false;
        }
    }

    private boolean isHeader(String markdown){
        if (markdown.startsWith("#")){
            return true;
        } else {
            return false;
        }
    }
}
