package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private HttpServletRequest httpServletRequest;

    private HttpSession httpSession;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        httpServletRequest = mock(HttpServletRequest.class);
        httpSession = mock(HttpSession.class);
    }

    @Test
    public void whenGetRegistrationPage() {
        var model = new ConcurrentModel();
        var view = userController.getRegistrationPage(model);

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenGetLoginPage() {
        var model = new ConcurrentModel();
        var view = userController.getLoginPage(model);

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenRegisterThenSaveUserAndGetRegistrationPage() {
        var user1 = new User(1, "Ivanov@mail.ru", "Ivanov Egor Sergeevich", "123456qwe");
        when(userService.save(user1)).thenReturn(Optional.of(user1));

        var model = new ConcurrentModel();
        var view = userController.register(user1, model);

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterFailThenError() {
        var user1 = new User(1, "Ivanov@mail.ru", "Ivanov Egor Sergeevich", "123456qwe");
        when(userService.save(user1)).thenThrow(Sql2oException.class);

        var model = new ConcurrentModel();
        var view = userController.register(user1, model);
        var message = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(message).isEqualTo(null);
        assertThat(userService.findByEmailAndPassword(user1.getEmail(), user1.getPassword())).isEqualTo(Optional.empty());
    }

    public void whenLogOutThenGetLogInPage() {
        var view = userController.logout(httpSession);

        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenLoginIsGoodThenGetVacanciesPage() {
        var user1 = new User(1, "Ivanov@mail.ru", "Ivanov Egor Sergeevich", "123456qwe");
        when(userService.findByEmailAndPassword(user1.getEmail(), user1.getPassword())).thenReturn(Optional.of(user1));
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user1, model, httpServletRequest);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenLoginAndUserIsNotExistsThenGetLoginPage() {
        var user1 = new User(1, "Ivanov@mail.ru", "Ivanov Egor Sergeevich", "123456qwe");
        when(userService.findByEmailAndPassword(user1.getEmail(), user1.getPassword())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUser(user1, model, httpServletRequest);
        var message = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(message).isEqualTo("Почта или пароль введены неверно");
    }

}