# Kanban

####
### General
#### The project was completed as part of a task on Yandex practicum.
The project is an analogue of the task manager. It identifies 3 types of tasks: tasks (common), subtasks and epic task 
that includes subtasks. Users can delete or add new tasks. They can also request information about tasks using the following endpoints:
- GET /tasks/task/ - returns common tasks;
- GET /tasks/subtask/ - returns subtasks;
- GET /tasks/epictask/ - returns epictasks;
- GET /tasks/task/?id - returns task with an ID equal to the specified one.
- GET /tasks/subtask/epic/?id - returns subtasks of epic task with an ID equal to the specified one.
- GET /tasks/history - returns the browsing history
- GET /tasks/ - returns all tasks

### Techology stack:
- [Java 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html) 
- [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson) (JSON library)

### KVServer
A separate server is used to store information. The basis was a ready-made implementation of KV Server from [this repository](https://github.com/praktikum-java/java-core-bighw-kvserver). It saves the manager state from files to a separate server.  