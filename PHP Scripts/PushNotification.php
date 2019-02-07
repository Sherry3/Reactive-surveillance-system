<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$regid="";

$q=mysql_query("SELECT * FROM User where UserId='usr1'") or Die("".mysql_error());
while($e=mysql_fetch_assoc($q))
{
    $output[]=$e;
    $regid=$e["RegistrationId"];
}

mysql_close() or Die("".mysql_error());

define("GOOGLE_API_KEY", "AIzaSyDkjOn7fhZadNdP_jglMsMYtDrZXKOd4tY"); 

$message=array("price" => "Hi sarthak");
$url = 'https://android.googleapis.com/gcm/send';

echo "I m here"."<br>";
echo $regid."<br>";
echo "I m also here"."<br>";

        $ids = array($regid);
        $fields = array(
            'registration_ids' => $ids,
            'data' => $message,
        );

 
echo "do u know I m also here".$ids[1]."<br>";

        $headers = array(
            'Authorization: key=' . 'AIzaSyDkjOn7fhZadNdP_jglMsMYtDrZXKOd4tY',
            'Content-Type: application/json'
        );
        //print_r($headers);
        // Open connection
        $ch = curl_init();
 
        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);
 
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
 
        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
 
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
 
        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
 
        // Close connection
        curl_close($ch);
        echo $result;

?>
