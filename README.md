# Git Blog

Git Blog generates a simple blog website hosted by `github pages` and lets you and your team manage this website. All have to do is run a Gitblog panel and add your team members to it. After that, you can start writing your blog posts.

## How it works

This tool uses [Sloth DB](https://github.com/idioglossia/sloth-db) json wrapper called [Json Sloth](https://github.com/idioglossia/json-sloth) as a storage where users, posts, tags and files are stored.
This data will be easily accessible through javascript and web browser. Therefore your data is pretty much dynamic in a way that users (blog writers) won't need to create different html files for each blog post.

Every new data you generate by gitblog will be saved into `db` directory, mostly in json format. This data often has reference to other datas too. For example, a user object contains list of post ids of a user. This `db` directory will be initialized into your repository root folder, along with Git Blog UI which is couple of Html, Css, and JS files. The UI has an API and Engine, one to read the data you generate into `db` directory, other to render pages based on that data.

An example of generated blog is available [here](https://idioglossia.github.io/git-blog-test/). Don't forget that the data there is just for example purpose and can be edited. 

## General Restrictions

Git Blog is not built to support large scale blog with lots of users and posts, so You should go easy on it! But it does support simple post managements and content organization. "How fast it can be" pretty much depends on how fast the viewer internet is. Since there is a network call needed for each individual piece of data, optimizing git blog will need some experience time. At this stage, I can't give more information about this, but surely it will always be my priority to make it faster.

## Version Restrictions

This section contains restrictions (or known problems) of Git Blog that is only for current version (_v0.1-beta.1_).

- New admins can remove all other admins including main one created through initialization.
- User passwords are encoded but saved along their profile information in `db`. So encoded version will be accessible through github repository in. This is not the safest way to store user passwords.
- Editing usernames is not possible at this stage. Once an user is created with an unique username, it is impossible to change that username in panel.
- Gitblog doesnt clone repositories for you. You can only pass cloned repository addresses from your machine.
- Pull and Push commands might not work. Gitblog commits repository after any change in data, but you should push the result yourself.
- **Gitblog doesnt work on Windows machines**. While It's not even tested on windows machine yet, there are linux commands for git service inside Gitblog that probably will break the tool on windows.

There are more restrictions to include in this list but you can see the list of full bugs, issues, and features I will be working on [here](https://github.com/idioglossia/git-blog/projects/1). The plan is to reduce these restrictions in next versions. 

There are other minor issues too, for example panel could be much better and usable through phone, code should get cleaner, and generated website could have more details and be more configurable. These will be done as well.
