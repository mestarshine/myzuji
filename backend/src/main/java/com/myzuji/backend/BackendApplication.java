package com.myzuji.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
@SpringBootApplication
public class BackendApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.out.println("                                开启足迹                \n" +
            "         ,----,                     ,---._           \n" +
            "       .'   .`|                   .-- -.' \\    ,---, \n" +
            "    .'   .'   ;                   |    |   :,`--.' | \n" +
            "  ,---, '    .'       ,--,        :    ;   ||   :  : \n" +
            "  |   :     ./      ,'_ /|        :        |:   |  ' \n" +
            "  ;   | .'  /  .--. |  | :        |    :   :|   :  | \n" +
            "  `---' /  ; ,'_ /| :  . |        :         '   '  ; \n" +
            "    /  ;  /  |  ' | |  . .        |    ;   ||   |  | \n" +
            "   ;  /  /--,|  | ' |  | |    ___ l         '   :  ; \n" +
            "  /  /  / .`|:  | : ;  ; |  /    /\\    J   :|   |  ' \n" +
            "./__;       :'  :  `--'   \\/  ../  `..-    ,'   :  | \n" +
            "|   :     .' :  ,      .-./\\    \\         ; ;   |.'  \n" +
            ";   |  .'     `--`----'     \\    \\      ,'  '---'    \n" +
            "`---'                        \"---....--'             \n" +
            "                                                     ");
        SpringApplication.run(BackendApplication.class, args);
    }
}
