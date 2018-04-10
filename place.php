<!-- 
AIzaSyCNrp3lXaXysAoNNxhVelfy1kDfCvMGPqk -->
<!-- AIzaSyAXWBbvjaVtGSGPybTbIpYXcnpiu4uBGaI -->
<!-- AIzaSyBujGjblZKUI3x7RPPeuuibylg57cxIikg -->

<?php
	$keyword=$distance=$locationtext=$category=$loc=$placeid=$linkname="";

		if(isset($_GET['placeid'])){
				
                $keyword=$_GET['keyword'];
                $category=$_GET['category'];
                $distance=$_GET['distance'];
              	$loc=$_GET['loc'];
              	if($loc=="location"){
					$locationtext=$_GET["locationtext"];
			}

        }


	
	$lat=$lon=0;
	if(isset($_POST["search"])){                   //Form completation
		$keyword=$_POST["keyword"];
		$distance=$_POST["distance"];
		if($distance=="") $distance=10;
		$category=$_POST["category"];
		$loc=$_POST["loc"];

		if($loc=="location"){
			$locationtext=$_POST["locationtext"];
		}
	}
		


			
?>


<html>
<head>
	<style type="text/css">
		body{
			text-align: center;
			margin: 0 auto;

		}

		h2{
			margin-top: 5px;
			margin-bottom: 5px;
		}

		a{
			text-decoration: none;
			color:#000000;
		}


		.block{
			text-align: center;
			border: 4px solid #CCCCCC;
			width: 600px;
			height: 210px;
			top:50px;
			margin: 0 auto;
			position: relative;
			background-color: #FAFAFA;
		}

		.line{  
		     
		    margin: 0 auto; 
		     
		    width: 580px;
		    height: 3px;
		    border: none;
		    border-top: 3px solid #CCCCCC;  
		   
		      
		}

		.form{
			text-align: left;
			margin-left: 7px;
			margin-top: 7px;
		}

		.location{	
		
			position: absolute;
		}

		.result{
			width: 1000px;
			margin: 0 auto;
		}
		.image{
			width: 610px;
			margin: 0 auto;
		}
		#map{
			position: absolute;
			height: 250px;
        	width: 300px;
		}

		.bg{
			width: 70px;  
			padding-top: 8px;

			height: 25px; 
			font-size:12;
			background-color: #F0F0F0;
		}

		.bg:hover{
			background-color: #DCDCDC;
		}



	</style>


	<title>Place</title>




</head>
<body>
	<form class="form" method="post" name="geodata">
		


	</form>

	<script type="text/javascript">
		var endlat;
		var endlon;

		window.onload=function(){     //Exectue when window open

			function loadJSON(url) {
				if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
					xmlhttp=new XMLHttpRequest();
					} 
				else {// code for IE6, IE5
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}

				xmlhttp.open("GET",url,false); // "synchronous"
				xmlhttp.send();
					
				jsonObj= JSON.parse(xmlhttp.responseText);
				return jsonObj; }

			jsonObj = loadJSON("http://ip-api.com/json");  //Get ip and address info

			if(jsonObj['status']=="success"){        //Get coordinates
				
				document.getElementById("searchbut").disabled=false;      //Enable search button
				document.getElementById("lat").value=jsonObj['lat'];
				document.getElementById("lon").value=jsonObj['lon'];

				

			}

				// if(document.getElementById("here").checked){
					
					
				// 	document.getElementById("loctext").disabled="disabled";
				// }
				// if(document.getElementById("locbut").checked)	{
				// 	alert("ss");
				// 	document.getElementById("loctext").disabled=false;
				// }	

		}

		function check(){                                //Disabling nearby input
			var rad=document.getElementsByName("loc");

			if(rad[0].checked){
				document.getElementById("loctext").disabled="disabled";
			}else if(rad[1].checked){
				document.getElementById("loctext").disabled=false;
			}

		}

		function isphoto(){
			var divobj=document.getElementById("phototable");
			if(divobj.style.display=='none'){
				divobj.style.display="inline-table";
				document.getElementById('phototext').innerHTML="click to hide photos";
				document.getElementById('photoicon').src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png";
			}else{
				divobj.style.display='none';
				document.getElementById('phototext').innerHTML="click to show photos";
				document.getElementById('photoicon').src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
			}


		}

		function isreview(){
			var divobj=document.getElementById("reviewtable");
			if(divobj.style.display=='none'){
				divobj.style.display="inline-table";
				document.getElementById('reviewtext').innerHTML="click to hide reviews";
				document.getElementById('reviewicon').src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png";
			}else{
				divobj.style.display='none';
				document.getElementById('reviewtext').innerHTML="click to show reviews";
				document.getElementById('reviewicon').src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
			}


		}

		function isshow(){
			divobj=document.getElementById("map");
			
			if(divobj.style.display=='none'){
				divobj.style.display="block";
				document.getElementById("direction").style.display="block";
				
				
			}else{
				divobj.style.display='none';
				document.getElementById("direction").style.display="none";
			}

		
		}

		function showmap(obj,rlat,rlon,num){
			endlat=rlat;
			endlon=rlon;
			rmap=document.getElementById('map');
			var actualLeft = obj.offsetLeft;
	　　　　 var current = obj.offsetParent;
			var actualTop = obj.offsetTop;

	　　　　 while (current !== null){
				actualLeft += current.offsetLeft;
				actualTop += current.offsetTop;
				current = current.offsetParent;
	　　　　 }

			rmap.style.left=actualLeft;
			rmap.style.top=actualTop+20;
			


			var uluru = {lat:"rlat", lng: "rlon"};
			uluru["lat"]=rlat;
			uluru["lng"]=rlon;
	        var map = new google.maps.Map(document.getElementById('map'), {
	          zoom: 12,
	          center: uluru
	        });
	        var marker = new google.maps.Marker({
	          position: uluru,
	          map: map
	        });


	        window.location.href="#ad"+num;
	        rmap.style.display="block";
	        document.getElementById("direction").style.display="block";

	        // walk=document.getElementById('walk')
	        // walk.style.left=actualLeft;
	        // walk.style.top=actualTop+20;
	        // bike=document.getElementById('bike')
	        // bike.style.left=actualLeft;
	        // bike.style.top=actualTop+40;
	        // drive=document.getElementById('drive')
	        // drive.style.left=actualLeft;
	        // drive.style.top=actualTop+60;
	       document.getElementById('direction').style.left=actualLeft;
	       document.getElementById('direction').style.top=actualTop+20;
	       

		}

		function direction(obj,startlat,startlon){

			var directionsService = new google.maps.DirectionsService();
			var directionsDisplay = new google.maps.DirectionsRenderer();
			var start = new google.maps.LatLng(startlat, startlon);
			var oceanBeach = new google.maps.LatLng(endlat, endlon);
			var mapOptions = {
			  zoom: 14,
			  center: start
			}
			var map = new google.maps.Map(document.getElementById('map'), mapOptions);
			directionsDisplay.setMap(map);
			
	  		var request = {
		        origin: start,
		        destination: oceanBeach,
		      // Note that Javascript allows us to access the constant
		      // using square brackets and a string value as its
		      // "property."
		        travelMode: google.maps.TravelMode[obj.id]
		    };
		    directionsService.route(request, function(response, status) {
		      if (status == 'OK') {
		        directionsDisplay.setDirections(response);
		      }
		    });


		}

	

	</script>

	<div class="block">
		<h2>Travel and Entertainment Search</h2>
		<hr class="line" />


		<form class="form" method="post" aaction="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
			<input type="hidden" id="lat" name="lat" value="">      <!-- Get coordinates from js -->

			<input type="hidden" id="lon" name="lon"  value="">


			Keyword  <input type="text" name="keyword" required="required" value="<?php echo $keyword;?>"> </br>
			
			Category  <select name="category">
						<option value="default" selected>default</option>
						<option value="cafe" <?php if($category=="cafe") echo "selected"; ?> >cafe</option>
						<option value="bakery" <?php if($category=="bakery") echo "selected"; ?>>bakery</option>
						<option value="restaurant" <?php if($category=="restaurant") echo "selected"; ?>>restaurant</option>
						<option value="beauty" <?php if($category=="beauty") echo "selected"; ?>>beauty salon</option>
						<option value="casino" <?php if($category=="casino") echo "selected"; ?>>casino</option>
						<option value="movie theater" <?php if($category=="movie theater") echo "selected"; ?>>movie theater</option>
						<option value="lodging" <?php if($category=="lodging") echo "selected"; ?>>lodging</option>
						<option value="airport" <?php if($category=="airport") echo "selected"; ?>>airport</option>
						<option value="train station" <?php if($category=="train station") echo "selected"; ?>>train station</option>
						<option value="subway station" <?php if($category=="subway station") echo "selected"; ?>>subway station</option>
						<option value="bus station" <?php if($category=="bus station") echo "selected"; ?>>bus station</option>
						
						</select> </br>

			Distance(mile) <input type="number" name="distance" placeholder="10" value="<?php echo $distance;?>"> from

			<span class="location">        <!--   nearbysearch input -->
			 <input type="radio" name="loc"  id="here" checked="checked"  value="here" onclick="javascript:check()">Here
			</br>
			 <input type="radio" name="loc" id="locbut" <?php if($loc=="location") echo"checked" ;?> value="location" onclick="javascript:check()">

			 <input type="text" id="loctext" name="locationtext" placeholder="location" disabled="disabled" required="required" value="<?php echo $locationtext;?>">
			</span>
			<br><br><br>

			&emsp;&emsp;&emsp;&emsp; <input type="submit" id="searchbut" name="search" value="Search" disabled="disabled">
			<?php if($loc=="location"){
				echo '<script> document.getElementById("loctext").disabled=false; </script>';}
				?>

			<input type="reset" name="clear" value="Clear" >

		</form>

	</div>


<br><br><br><br>
<?php

	if(isset($_POST["search"])){


		if($_POST["loc"]=="location"){          //Get place coordinates
			// $locationtext=$_POST["locationtext"];
			$location=htmlspecialchars($_POST["locationtext"]);
			$location=str_replace(',', ' ', $location);
			$location=urlencode($location);

			//echo $location;
			$geocontent=file_get_contents('https://maps.googleapis.com/maps/api/geocode/json?address='.$location.'&key=AIzaSyBujGjblZKUI3x7RPPeuuibylg57cxIikg');
			$geojson=json_decode($geocontent,true);
			// var_dump($geojson2);			

			// foreach ($geojson as $key => $value) {
			// 	# code...
			// 	echo $key."  ". $value;
			// }
			if ($geojson["status"]=="OK");

				$lat=$geojson["results"][0]["geometry"]["location"]["lat"];
				$lon=$geojson["results"][0]["geometry"]["location"]["lng"];
				// foreach ($lat as $item) {
				// 	echo $item;
				// 	# code...
				// }	
		}

		if($_POST["loc"]=="here"){       //Get "here" coordinates 

			$lat= $_POST["lat"];
			$lon= $_POST["lon"];
		}

		if($lat==0 || $lon ==0){          //If failed to get coordinates
		?>
		
		<span>Failed to get the location!</span>
<?php		
		}

		
		$distanceMeter=($distance*1609)>50000?50000:$distance*1609;;          //Trans distance
        $keyword=urlencode($keyword);
        $category=urlencode($category);
		// echo $distance;
		$nearbycontent=file_get_contents('https://maps.googleapis.com/maps/api/place/nearbysearch/json?location='.$lat.','.$lon.'&radius='.$distanceMeter.'&type='.$category.'&keyword='.$keyword.'&key=AIzaSyCxF6BWr5FLMiSJyD5DYjDvPDVQHUB3Bco');   //Get nearby place json

		// var_dump($nearbycontent);

		// echo $nearbycontent[0];



?>



<script type="text/javascript">
	var nearbyjson=<?php echo $nearbycontent  ?>;    //Parse nearby json in js
	var nearbyresults=nearbyjson.results;
	var startlat=<?php echo $lat ?>;
	var startlon=<?php echo $lon ?>;

	html_text="<table border='2' class='result' cellspacing='0'  bordercolor='#DBDBDB' style='border-collapse:collapse'>";

	if(nearbyjson.status!="OK"){
		// alert(nearbyjson.status);
		html_text+="<tr > <td bgcolor='#EFEFEF' align='center'>No Records has been found</td></tr></table> ";

	}else{
	
	html_text+="<thead > <tr height='30px'> <th> Category</th> <th>Name</th> <th>Address</th> </thead>  ";
	html_text+="<tbody>";

	for(i=0; i<nearbyresults.length;i++ ){


			tourl=escape(nearbyresults[i]["name"]);
			rlat=nearbyresults[i]["geometry"]["location"]["lat"];
			rlng=nearbyresults[i]["geometry"]["location"]["lng"];

			html_text+="<tr height='45px' >";
			html_text+="<td width='100x'><img src='"+ nearbyresults[i]["icon"] +"' width='55' height='40' ></td>";
			html_text+="<td width='350px'> &emsp; <a href='place.php?placeid="+nearbyresults[i]["place_id"]+"&keyword=<?php echo $keyword;?>&category=<?php echo $category;?>&distance=<?php echo $distance;?>&loc=<?php echo $loc;?>&linkname="+ tourl+"<?php if($loc=="location") echo "&locationtext=".$locationtext; ?>  ' > "+ nearbyresults[i]["name"]+ "</a> </td>";
			html_text+="<td width='550px'> &emsp;  <a  onclick='javascript:showmap(this,"+rlat+","+rlng+")'>"+ nearbyresults[i]["vicinity"]+" </a></td>";
			html_text+="</tr>";  


		}
		html_text+="</tbody> </table>"
	}
	

	document.write(html_text);




</script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBujGjblZKUI3x7RPPeuuibylg57cxIikg">
</script>


<?php


	}	
	if(isset($_GET['placeid'])&& !isset($_POST["search"])){
		$placeid=$_GET['placeid'];
		$linkname=urldecode($_GET['linkname']);
		

		$nearbydetails=file_get_contents("https://maps.googleapis.com/maps/api/place/details/json?placeid=".$placeid."&key=AIzaSyCxF6BWr5FLMiSJyD5DYjDvPDVQHUB3Bco");
		
		
		$detailjson=json_decode($nearbydetails,true);
		$photolen=0;
		
		if($detailjson["status"]=="OK"){

			if(array_key_exists("photos", $detailjson["result"])){

				$photos=$detailjson["result"]["photos"];

				
				$photolen=min(count($photos), 5);
				
				for($i=0; $i<$photolen; $i++){
					// var_dump($photos[$i]);
					$filename="image".$i.".jpeg";
					// echo $photos[$i]["photo_reference"];
					$imagecontent=file_get_contents("https://maps.googleapis.com/maps/api/place/photo?maxwidth=750&photoreference=".$photos[$i]["photo_reference"]."&key=AIzaSyCxF6BWr5FLMiSJyD5DYjDvPDVQHUB3Bco");
					file_put_contents($filename , $imagecontent);
				}
			}
		}

?>
		<script type="text/javascript">
			var photolen=<?php echo $photolen; ?>;
			var linkname="<?php echo $linkname; ?>";
			var nearbydetails=<?php echo $nearbydetails; ?>;
			tmp=nearbydetails.result;

			reviewlen=0;
			if(nearbydetails.status=="OK" && tmp.hasOwnProperty("reviews")){
			reviews=tmp.reviews;	
			reviewlen=Math.min(reviews.length, 5);}
			
			html_text="<h4>"+linkname + "</h4>"


			html_text+="<a href='javascript:isreview()' text-decoration='none'><div><span id='reviewtext'>click to show reviews</span><br><img id='reviewicon' src='http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png' width='34' height='20'> </div></a> ";
			html_text+="<table border='2' id='reviewtable' class='image' cellspacing='0'  bordercolor='#DBDBDB' style='border-collapse:collapse; display:none'>";

			if(reviewlen!=0){
				for(i=0; i<reviewlen; i++){
					html_text+="<tr height='50'><td align='center' > <img src=' "+reviews[i]["profile_photo_url"]+ " ' width='40' height='40'  ><span class='font1' >"+ reviews[i]["author_name"] +"</span> </td></tr>"	;
					html_text+="<tr height='30'><td >"+ reviews[i]["text"] +" </td></tr>";

				}
			}else{
				html_text+="<tr><td align='center'><span class='font1' >No Reviews Found</span></td><tr>";
			}
			html_text+="</table> <br>";



			html_text+="<a href='javascript:isphoto()' text-decoration=‘none’><div><span id='phototext'>click to show photos</span><br><img id='photoicon' src='http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png' width='34' height='20'> </div></a>";
			html_text+="<table border='2' id='phototable' class='image' cellspacing='0'  bordercolor='#DBDBDB' style='border-collapse:collapse; display:none'>";

			if(photolen!=0){
				
				for(i=0; i<photolen; i++){
					html_text+="<tr height='470'><td align='center' > <a href='..\\image"+i+".jpeg' target='view_window'><img src='image" +i+".jpeg'  width='570' height='430'  > </a> </td></tr>"
				}

				
			}else{
				html_text+="<tr><td align='center'><span class='font1' >No Photos Found</span></td><tr>";
			}
			html_text+="</table>";

			document.write(html_text);

		</script>



<?php

	}


	
 ?>

<div id='map' onclick="javascript:isshow()" style="display: none">


</div>

<div id="direction" style="position: absolute; display: none">
<div class="bg" id="WALKING" onclick="javascript:direction(this,startlat,startlon)">Walk There</a></div>
<div class="bg" id="BICYCLING" onclick="javascript:direction(this,startlat,startlon)">Bike There</div>
<div class="bg" id="DRIVING" onclick="javascript:direction(this,startlat,startlon)">Drive There</div>
</div>
<br><br>
</body>
</html>

