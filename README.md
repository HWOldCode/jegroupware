# jegroupware
jegroupware - Egroupware Java Client

## Info
This client is testing by EGroupware 14.2 last update to 2015-06-10.

## License
http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)

## Used
I used this library for my projects:
- EgwWinLogin & ELogin EGroupware App/Module
- Notificater for OS System (new version in future)
- EGroupware Workflow Java Node Server (future)

View to: https://www.hw-softwareentwicklung.de/projekte/egwwinlogon/

## Build
First open the project in Netbeans (Version 8.0.2+) with java version 8.<br>
Then clean and build this project in netbeans. <br>
By Run and Debug you open the example 1.<br>

## Add as Library
Open your Netbeans project and click right "Properties". <br>
Then click "libraries" -> "Add Jar/Folder" select "jegroupware.jar".<br>
Finish to use EGroupware Java Client.<br>

## Connection to Egroupware
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
TODO
