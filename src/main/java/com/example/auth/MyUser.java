package com.example.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class MyUser {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;

    public MyUser(Long id, String username, String password, String email, String phone, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // MyUser의 일부분만 채워서 만들기 위한 Builder 클래스
    public static class MyUserBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private String phone;
        private String address;

        public MyUserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MyUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MyUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MyUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MyUserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public MyUserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public MyUser build() {
            return new MyUser(
                    id,
                    username,
                    password,
                    email,
                    phone,
                    address
            );
        }
    }

    public static MyUserBuilder builder() {
        return new MyUserBuilder();
    }

    public static void main(String[] args) {
//        // id = 1L, username = "alex";
//        MyUserBuilder builder = new MyUserBuilder();
//        builder.id(1L)
//                .username("alex")
//                .email("alex@a.a")
//                .address("Seoul");
//
//        MyUser user = builder.build();
        MyUser user = MyUser.builder()
                .id(1L)
                .username("alex")
                .email("a@a.a")
                .build();
        user = new MyUser(
                1L,
                "alex",
                null,
                "a@a.a",
                null,
                null
        );
    }
}
