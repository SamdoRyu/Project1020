import controller.MainController;

// 프로그램 시작점. 로그인 없이 바로 메인 메뉴를 띄운다.
public class Main {
    public static void main(String[] args) {
        new MainController().run();
    }
}
