package config;

public enum Path {
    BASE_URL("https://stellarburgers.nomoreparties.site"),
    LOGIN("/api/auth/login"),
    REGISTER("/api/auth/register"),
    USER("/api/auth/user"),
    ORDERS("/api/orders");
    private final String title;

    Path(String path) {
        this.title = path;
    }

    public String getTitle() {
        return title;
    }
}
