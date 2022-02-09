rootProject.name = "dm"
include("collections")
include("dm-common")
include("dm-file-core")
project(":dm-file-core").projectDir = file("dm-file/dm-file-core")
include("dm-file-stream")
project(":dm-file-stream").projectDir = file("dm-file/dm-file-stream")
include("dm-file-spring-boot-starter")
project(":dm-file-spring-boot-starter").projectDir = file("dm-file/dm-file-spring-boot-starter")
include("dm-autoconfigure")
include("dm-authority-common")

include("dm-security-core")
project(":dm-security-core").projectDir = file("dm-security/dm-security-core")
include("dm-security-web")
project(":dm-security-web").projectDir = file("dm-security/dm-security-web")
include("dm-multi-datasource")
include("dm-security-mp")
project(":dm-security-mp").projectDir = file("dm-security/dm-security-mp")
include("dm-security-oauth2-core")
project(":dm-security-oauth2-core").projectDir = file("dm-security/dm-security-oauth2-core")

include("dm-security-oauth2-resource")
project(":dm-security-oauth2-resource").projectDir = file("dm-security/dm-security-oauth2-resource")

include("dm-security-oauth2-client")
project(":dm-security-oauth2-client").projectDir = file("dm-security/dm-security-oauth2-client")

include("dm-security-spring-boot-starter")
project(":dm-security-spring-boot-starter").projectDir = file("dm-security/dm-security-spring-boot-starter")

include("dm-oauth2-resource-server-spring-boot-starter")
project(":dm-oauth2-resource-server-spring-boot-starter").projectDir =
    file("dm-security/dm-oauth2-resource-server-spring-boot-starter")

include("dm-oauth2-client-spring-boot-starter")
project(":dm-oauth2-client-spring-boot-starter").projectDir = file("dm-security/dm-oauth2-client-spring-boot-starter")

project(":dm-multi-datasource").projectDir = file("dm-jdbc/dm-multi-datasource")

include("dm-multi-datasource-jpa-support")
project(":dm-multi-datasource-jpa-support").projectDir = file("dm-jdbc/dm-multi-datasource-jpa-support")
include("dm-region")
include("dm-uap-common")

include(":dm-notification-api")
project(":dm-notification-api").projectDir = file("dm-notification/dm-notification-api")

include(":dm-notification-client")
project(":dm-notification-client").projectDir = file("dm-notification/dm-notification-client")

include(":dm-notification-server")
project(":dm-notification-server").projectDir = file("dm-notification/dm-notification-server")

include(":dm-todo-task-api")
project(":dm-todo-task-api").projectDir = file("dm-todo-task/dm-todo-task-api")

include(":dm-todo-task-client")
project(":dm-todo-task-client").projectDir = file("dm-todo-task/dm-todo-task-client")

include(":dm-todo-task-server")
project(":dm-todo-task-server").projectDir = file("dm-todo-task/dm-todo-task-server")
