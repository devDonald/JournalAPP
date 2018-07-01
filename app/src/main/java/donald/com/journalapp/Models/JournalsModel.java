package donald.com.journalapp.Models;

public class JournalsModel {
    private String content, category, date;

    public JournalsModel() {
    }

    public JournalsModel(String content, String category, String date) {
        this.content = content;
        this.category = category;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
