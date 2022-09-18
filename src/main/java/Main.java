public class Main {

    public static void main(String args[]) {
        String loc = "src/main/resources/";
//        Files.createDirectories(Paths.get("/Your/Path/Here"));
        Reader.readAndWriteHTMLtoJSON("https://www.zara.com/us/en/narciso-rodriguez-oversized-jumpsuit-p08132811.html", loc + "zara");

    }
}
