package com.spark.InventorySalesManager.config;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(openApi -> {
                    Paths filteredPaths = new Paths();

                    openApi.getPaths().forEach((path, pathItem) -> {
                        if (path.equals("/api/auth/register")) {
                            return; // skip register for public
                        }
                        PathItem filteredItem = new PathItem();

                        // Allow only GET and POST for public users
                        if (pathItem.getGet() != null) {
                            filteredItem.setGet(pathItem.getGet());
                        }

                        if (pathItem.getPost() != null) {
                            filteredItem.setPost(pathItem.getPost());
                        }

                        // block PUT, DELETE, PATCH for public

                        if (hasAnyOperation(filteredItem)) {
                            filteredPaths.addPathItem(path, filteredItem);
                        }
                    });

                    openApi.setPaths(filteredPaths);
                })
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-api")
                .pathsToMatch("/api/**") // No customizer — show all methods
                .build();
    }

    private boolean hasAnyOperation(PathItem item) {
        return item.getGet() != null || item.getPost() != null ||
                item.getPut() != null || item.getDelete() != null ||
                item.getPatch() != null;
    }
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public-api")
//                .addOpenApiCustomizer(openApi -> {
//                    Paths filteredPaths = new Paths();
//
//                    openApi.getPaths().forEach((path, pathItem) -> {
//                        PathItem filteredItem = new PathItem();
//
//                        if (isPublic(pathItem.getGet())) filteredItem.setGet(pathItem.getGet());
//                        if (isPublic(pathItem.getPost())) filteredItem.setPost(pathItem.getPost());
//                        if (isPublic(pathItem.getPut())) filteredItem.setPut(null);
//                        if (isPublic(pathItem.getDelete())) filteredItem.setDelete(null);
//                        if (isPublic(pathItem.getPatch())) filteredItem.setPatch(pathItem.getPatch());
//
//                        if (hasAnyOperation(filteredItem)) {
//                            filteredPaths.addPathItem(path, filteredItem);
//                        }
//                    });
//
//                    openApi.setPaths(filteredPaths);
//                })
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("admin-api")
//                .addOpenApiCustomizer(openApi -> {
//                    Paths filteredPaths = new Paths();
//
//                    openApi.getPaths().forEach((path, pathItem) -> {
//                        PathItem filteredItem = new PathItem();
//
//                        if (isAdmin(pathItem.getGet())) filteredItem.setGet(pathItem.getGet());
//                        if (isAdmin(pathItem.getPost())) filteredItem.setPost(pathItem.getPost());
//                        if (isAdmin(pathItem.getPut())) filteredItem.setPut(pathItem.getPut());
//                        if (isAdmin(pathItem.getDelete())) filteredItem.setDelete(pathItem.getDelete());
//                        if (isAdmin(pathItem.getPatch())) filteredItem.setPatch(pathItem.getPatch());
//
//                        if (hasAnyOperation(filteredItem)) {
//                            filteredPaths.addPathItem(path, filteredItem);
//                        }
//                    });
//
//                    openApi.setPaths(filteredPaths);
//                })
//                .build();
//    }
//
//
//
//    private boolean isPublic(io.swagger.v3.oas.models.Operation op) {
//        return op != null && op.getTags() != null && op.getTags().contains("public");
//    }
//
//    private boolean isAdmin(io.swagger.v3.oas.models.Operation op) {
//        return op != null && op.getTags() != null && (op.getTags().contains("admin") || op.getTags().contains("public"));
//    }
//
//    private boolean hasAnyOperation(PathItem item) {
//        return item.getGet() != null || item.getPost() != null ||
//                item.getPut() != null || item.getDelete() != null ||
//                item.getPatch() != null;
//    }
}
