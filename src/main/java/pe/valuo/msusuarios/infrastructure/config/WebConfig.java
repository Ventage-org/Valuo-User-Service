package pe.valuo.msusuarios.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/** Sirve los archivos subidos (firmas) como recursos estáticos en /uploads/**. */
@Configuration
@EnableConfigurationProperties(ValuoProperties.class)
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ValuoProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = Path.of(properties.storage().directorio()).toAbsolutePath().normalize().toUri().toString();
        if (!ubicacion.endsWith("/")) {
            ubicacion += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(ubicacion);
    }
}
