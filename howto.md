# Практика Git

## Порядок выполнения команд
1. Делаем форк репозитория `https://github.com/Kotlin-Polytech/KotlinAsFirst2020` и клонируем себе на пк через `git clone`
2. Добавляем upstream: `git remote add upstream-my https://github.com/Zar1official/KotlinAsFirst2021`
3. Производим fetch из апстрима `git fetch upstream-my`
4. Создаем ветку `backport` и переходим в нее: `git checkout -b backport`
5. Переносим коммиты в backport: `git cherry-pick d535f3592006b8f2593c9f881d72c05164aadc13...FETCH_HEAD`
6. Добавляем второй апстрим:  git remote add upstream-theirs `https://github.com/KimDamir/KotlinAsFirst2020`
7. Делаем `fetch upstream-theirs`
8. Возвращемся в master: `git checkout master`
9. Мерджим: `git merge backport upstream-theirs/master`
10. Разрешаем конфликты
11. git commit + git push
13. Запускаем `git remote -v `
14. git commit + git push
15. Пишем этот файл)
