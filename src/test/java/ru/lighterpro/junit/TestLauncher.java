package ru.lighterpro.junit;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

// Мы видели на предыдущих занятиях, что IntelliJ IDEA позволяет нам запускать тесты,
// как будто мы запускаем метод main. То же самое и с Maven/Gradle - оба инструмента
// умеют запускать наши тесты. Теперь мы разберемся, как именно они это делают,
// подробно изучая архитектуру JUnit 5 и слой Launcher API в нем.

public class TestLauncher {
    public static void main(String[] args) {
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
//                .selectors(DiscoverySelectors.selectClass(UserServiceTest.class))
                .selectors(DiscoverySelectors.selectPackage("ru.lighterpro.junit"))
                .build();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.execute(request, listener);

        try (PrintWriter printWriter = new PrintWriter(System.out)) {
            listener.getSummary().printTo(printWriter);
        }
    }
}
