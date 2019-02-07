<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

echo $_REQUEST['username'].'<br>'.$_REQUEST['password'];

$q=mysql_query("SELECT * FROM User where UserId='".$_REQUEST['username']."' and Password='".$_REQUEST['password']."'") or Die("".mysql_error());

header("Location: video.html");    

print(json_encode($output));

mysql_close() or Die("".mysql_error());
?>