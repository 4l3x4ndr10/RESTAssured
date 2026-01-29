package api.pojo;

public class SuccessReg {
    private Integer id;
    private String token;

    public SuccessReg(String token, Integer id) {
        this.token = token;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
