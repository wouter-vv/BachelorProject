
<?php 
require "connection.php";
$mysql_qry = "select * from Rooms";
$result = mysqli_query($conn ,$mysql_qry);
if(mysqli_num_rows($result) > 0) {
    $rooms = "Rooms: ";
    while($row = $result->fetch_assoc()) {
        $rooms .= $row["nameRoom"] . " ";
        
    }
    echo $rooms;
}
else {
echo "Roomsnot successfully received";
}
 
?>