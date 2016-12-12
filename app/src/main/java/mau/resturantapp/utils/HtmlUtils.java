package mau.resturantapp.utils;

public class HtmlUtils {
    public static String escapeHtml(CharSequence text) {
        StringBuilder sb = new StringBuilder();

        withinStyle(sb, text, 0, text.length());

        return sb.toString();
    }

    private static void withinStyle(StringBuilder sb, CharSequence text, int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else if (c == '&') {
                sb.append("&amp;");
            } else if (c >= 0xD800 && c <= 0xDFFF) {
                if (c < 0xDC00 && i + 1 < end) {
                    char d = text.charAt(i + 1);
                    if (d >= 0xDC00 && d <= 0xDFFF) {
                        i++;
                        int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                        sb.append("&#").append(codepoint).append(";");
                    }
                }
            } else if (c > 0x7E || c < ' ') {
                sb.append("&#").append((int) c).append(";");
            } else if (c == ' ') {
                while (i + 1 < end && text.charAt(i + 1) == ' ') {
                    sb.append("&nbsp;");
                    i++;
                }

                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
    }
}
