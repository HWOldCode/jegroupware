# jegroupware
jegroupware - Egroupware Java Client libary, use to control and remote your egroupware, in backend work http client, all function represent as api.

## Info
This client is testing by EGroupware (14.2 old and new 17.1) last update to 2017-12-18.<br>
![EGroupware](http://www.egroupware.org/wp-content/themes/egroupware/images/logo.svg "Go To EGRoupware")<br>
http://www.egroupware.org

## Update (2017-12-18)
add Filemanager:
- delete
- dir
- upload file
- download file

bugfix and code clear, remove/change deprecated

## License
http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)

## Used
I used this library for my projects:
- EgwWinLogin & ELogin EGroupware App/Module
- Notificater for OS System (new version in future)
- EGroupware Workflow Java Node Server (future)
- Android Client (future)?

View to: https://www.hw-softwareentwicklung.de/projekte/egwwinlogon/

## Build
First open the project in Netbeans (Version 8.0.2+) with java version 8.<br>
Then clean and build this project in netbeans. <br>
By Run and Debug you open the example 1.<br>

## Add as Library
Open your Netbeans project and click right "Properties". <br>
Then click "libraries" -> "Add Jar/Folder" select "jegroupware.jar".<br>
Finish to use EGroupware Java Client.<br>

## Help
- report errors
- report ideas
- talk with me
- fork and commit new code :)
- help egroupware ;)

## Connection to Egroupware (short Example)
<pre>
Egroupware egw = Egroupware.getInstance(new EgroupwareConfig(
    "http://mydomain/egroupware/",
    "default",
    "admin",
    "password"
    ));

try {
  egw.login();

  if( egw.isLogin() ) {
  ......
</pre>

## Example
See/Check Example1 (login, delete file, dir file list, upload file and download file):
- com.jegroupware.example.test_egw_1.java
