package entity;

public class User {
        private String name;
        private String username;
        private String password;
        private String user_type;

    public User(String name, String username, String password, String userType) {
        this.name = name;
        this.username = username;
        this.password = password;
        user_type = userType;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}


