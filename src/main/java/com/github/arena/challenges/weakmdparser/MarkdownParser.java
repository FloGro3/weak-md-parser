package com.github.arena.challenges.weakmdparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die einen String mit einer Markdown-Syntax in einen HTML-String übersetzt
 */
public class MarkdownParser {

    public MarkdownParser() {
    }

    /**
     * Hauptmethode der Klasse. Wandelt den übergebenen String in einen HTML-String um
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
    public String parse(String markdown) {
        String[] lines = markdown.split("\n");
        String result = "";
        List<String> listItems = new ArrayList<>();

        //StringBuilder oder StringBuffer einsetzen für bessere Performance bei großen Eingabemengen
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

    /**
     * Wandelt den übergebenen String in ein HTML-Heading um. Die # am Anfang geben dabei die Art des Headings an. # -> h1; ## -> h2 ...
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
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

    /**
     * Wandelt den übergebenen String in ein HTML-ListItem um. Der String muss mit * beginnen.
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
    private String parseListItem(String markdown) {
        return "<li>" + markdown.substring(2) + "</li>";
    }

    /**
     * Wandelt den übergebenen String in einen HTML-Paragraph um.
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
    private String parseParagraph(String markdown) {
        return "<p>" + markdown + "</p>";
    }

    /**
     * Bettet die Wörter entsprechend der Notation im String in HTML-bold oder HTML-italic ein. _Wort_ -> Wort wird in italic eingebettet; __Wort__ -> Wort wird in bold eingebettet.
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
    private String parseInnerContent(String markdown) {
        String boldParsed = parseBold(markdown);
        return parseItalic(boldParsed);
    }

    /**
     * Bettet die Wörter, die von einem Unterstrich umgeben sind, in HTML-italic ein.
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
    private String parseItalic(String markdown){
        //Um die Methode vor parseBold() verwenden zu können, müsste das Erkennen des TeilStrings verändert werden, z.B. per RegExp
        String lookingFor = "_(.+)_";
        String update = "<em>$1</em>";
        return markdown.replaceAll(lookingFor, update);
    }

    /**
     * Bettet die Wörter, die von zwei Unterstrichen umgeben sind, in HTML-italic ein.
     * @param markdown String mit der Markdown-Syntax, der übersetzt werden soll
     * @return Übersetzter HTML-String
     */
    private String parseBold(String markdown){
        String lookingFor = "__(.+)__";
        String update = "<strong>$1</strong>";
        return markdown.replaceAll(lookingFor, update);
    }

    /**
     * Bettet die übergebenen Listitems in eine HTML-unorderdList ein.
     * @param listItems Liste mit den Items die eingebettet werden sollen
     * @return Übersetzter HTML-String
     */
    private String parseList(List<String> listItems){
        //Für viele aufeinander folgende ListItems besser einen StringBuilder oder StringBuffer für bessere Performance einsetzen
        String result = "";
        for (String listItem:listItems){
            result += parseListItem(listItem);
        }
        return "<ul>" + result + "</ul>";
    }

    /**
     * Prüft, ob der übergebene String die Markdown-Syntax für ein HTML-ListItem erfüllt.
     * @param markdown zu prüfender String
     * @return True, wenn der String die Markdown-Syntax für ein HTML-Listitem erfüllt, ansonsten false
     */
    private boolean isListItem(String markdown){
        return markdown.startsWith("*");
    }

    /**
     * Prüft, ob der übergebene String die Markdown-Syntax für ein HTML-Heading erfüllt.
     * @param markdown zu prüfender String
     * @return True, wenn der String die Markdown-Syntax für ein HTML-Heading erfüllt, ansonsten false
     */
    private boolean isHeader(String markdown){
        return markdown.startsWith("#");
    }
}
