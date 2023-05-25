const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");

	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");

	}
};

function deleteHotel(hId) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this hotel..!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/admin/deletehotel/" + hId;
			} else {
				swal("Your Hotel  is safe!");
			}
		});

}


function deletetrain(tId) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this Train..!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/admin/deletetrain/" + tId;
			} else {
				swal("Your Train  is safe!");
			}
		});

}

function deletebus(bId) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this Bus..!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/admin/deletebus/" + bId;
			} else {
				swal("Your Bus  is safe!");
			}
		});

}


function deleteflight(fId) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this Flight..!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/admin/deleteflight/" + fId;
			} else {
				swal("Your Flight  is safe!");
			}
		});

}



function deletepackage(pId) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this Holiday Package..!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/admin/deletepackage/" + pId;
			} else {
				swal("Your Holiday Package  is safe!");
			}
		});

}


const paymentStart = () => {
	let amount = $('#add-sum').html();
	let busId = $('#busId').val();
	let noOfSeats = $('#num-one').val();
	let date = $('#nus').val();
	let address = $('#descr').val();

	if (amount == "" || amount == null || amount <= 0 || busId == "" || busId == null || noOfSeats == "" || noOfSeats == null ||
		date == "" || date == null || address == "" || address == null) {

		swal("Failed!", "Please Fill All The Details Correctly!", "error")

		return;
	}
	//using ajack to request order

	$.ajax(
		{

			url: '/user/create_order',
			data: JSON.stringify({ amount: amount, address: address, date: date, busId: busId, noOfSeats: noOfSeats, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				//succes thn function call
				console.log(response)
				if (response.status == "created") {
					//open payment form
					let options = {
						key: 'rzp_test_jhw2NDpjfg1Cl9',
						amount: response.amount,
						currency: "INR",
						name: "MyTrip Booking",
						description: "Donation",
						image: "",
						order_id: response.id,

						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("payemt sucessfulle")

							//swal("Good job!", "Payment successfully done !", "success");

							updatePayementServer(response.razorpay_order_id, "paid");


						},

						prefill: {
							name: "",
							email: "",
							contact: ""
						},
						notes: {
							address: "Sameer DOmale"
						},
						theme: {
							color: "#3399cc"
						}

					};
					let rzp = new Razorpay(options);
					rzp.open();
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);

						swal("Failed!!", "OPPs!! Payment failed!", "error");


					});
				}
			},
			error: function(error) {
				console.log("error occur")
				alert("something went wrong !!")
			}
		}
	)

};


function updatePayementServer(orderId, status) {

	$.ajax({

		//create url to sent the request to the server with data

		url: "/user/update_order",

		// ha data ahe apla

		data: JSON.stringify({ orderId: orderId, status: status }),

		contentType: 'application/json',

		type: 'post',

		dataType: 'json',

		//after succes

		success: function(response) {

			swal("Good job!", "Payment successfully done !", "success");

		},

		error: function(error) {

			swal("Failed!", "Payment SuccesFul But We Did Not Capture Details As Sooon As!", "error");

		},

	});

}

const flightpaymentStart = () => {
	let amount = $('#add-sum').html();
	let flightId = $('#flightId').val();
	let noOfSeats = $('#num-one').val();
	let date = $('#nus').val();


	if (amount == "" || amount == null || amount <= 0 || flightId == "" || flightId == null || noOfSeats == "" || noOfSeats == null ||
		date == "" || date == null) {

		swal("Failed!", "Please Fill All The Details Correctly!", "error")

		return;
	}
	//using ajack to request order

	$.ajax(
		{

			url: '/user/create_flightorder',
			data: JSON.stringify({ amount: amount, date: date, flightId: flightId, noOfSeats: noOfSeats, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				//succes thn function call
				console.log(response)
				if (response.status == "created") {
					//open payment form
					let options = {
						key: 'rzp_test_jhw2NDpjfg1Cl9',
						amount: response.amount,
						currency: "INR",
						name: "MyTrip BOOKING",
						description: "Donation",
						image: "",
						order_id: response.id,

						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("payemt sucessfulle")

							//swal("Good job!", "Payment successfully done !", "success");

							flightupdatePayementServer(response.razorpay_order_id, "paid");


						},

						prefill: {
							name: "",
							email: "",
							contact: ""
						},
						notes: {
							address: "Sameer DOmale"
						},
						theme: {
							color: "#3399cc"
						}

					};
					let rzp = new Razorpay(options);
					rzp.open();
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);

						swal("Failed!!", "OPPs!! Payment failed!", "error");


					});
				}
			},
			error: function(error) {
				console.log("error occur")
				alert("something went wrong !!")
			}
		}
	)

};


function flightupdatePayementServer(orderId, status) {

	$.ajax({

		//create url to sent the request to the server with data

		url: "/user/update_flightorder",

		// ha data ahe apla

		data: JSON.stringify({ orderId: orderId, status: status }),

		contentType: 'application/json',

		type: 'post',

		dataType: 'json',

		//after succes

		success: function(response) {

			swal("Good job!", "Payment successfully done !", "success");

		},

		error: function(error) {

			swal("Failed!", "Payment SuccesFul But We Did Not Capture Details As Sooon As!", "error");

		},

	});

}


const trainpaymentStart = () => {
	let amount = $('#add-sum').html();
	let trainId = $('#trainId').val();
	let noOfSeats = $('#num-one').val();
	let date = $('#nus').val();


	if (amount == "" || amount == null || amount <= 0 || trainId == "" || trainId == null || noOfSeats == "" || noOfSeats == null ||
		date == "" || date == null) {

		swal("Failed!", "Please Fill All The Details Correctly!", "error")

		return;
	}
	//using ajack to request order

	$.ajax(
		{

			url: '/user/create_traintorder',
			data: JSON.stringify({ amount: amount, date: date, trainId: trainId, noOfSeats: noOfSeats, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				//succes thn function call
				console.log(response)
				if (response.status == "created") {
					//open payment form
					let options = {
						key: 'rzp_test_jhw2NDpjfg1Cl9',
						amount: response.amount,
						currency: "INR",
						name: "MyTrip BOOKING",
						description: "Donation",
						image: "",
						order_id: response.id,

						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("payemt sucessfulle")

							//swal("Good job!", "Payment successfully done !", "success");

							trainupdatePayementServer(response.razorpay_order_id, "paid");


						},

						prefill: {
							name: "",
							email: "",
							contact: ""
						},
						notes: {
							address: "Sameer DOmale"
						},
						theme: {
							color: "#3399cc"
						}

					};
					let rzp = new Razorpay(options);
					rzp.open();
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);

						swal("Failed!!", "OPPs!! Payment failed!", "error");


					});
				}
			},
			error: function(error) {
				console.log("error occur")
				alert("something went wrong !!")
			}
		}
	)

};


function trainupdatePayementServer(orderId, status) {

	$.ajax({

		//create url to sent the request to the server with data

		url: "/user/update_trainorder",

		// ha data ahe apla

		data: JSON.stringify({ orderId: orderId, status: status }),

		contentType: 'application/json',

		type: 'post',

		dataType: 'json',

		//after succes

		success: function(response) {

			swal("Good job!", "Payment successfully done !", "success");

		},

		error: function(error) {

			swal("Failed!", "Payment SuccesFul But We Did Not Capture Details As Sooon As!", "error");

		},

	});

}




const hotelpaymentStart = () => {
	let amount = $('#add-sum').html();
	let hotelId = $('#hotelId').val();
	let noOfSeats = $('#num-one').val();
	let date = $('#nus').val();


	if (amount == "" || amount == null || amount <= 0 || hotelId == "" || hotelId == null || noOfSeats == "" || noOfSeats == null ||
		date == "" || date == null) {

		swal("Failed!", "Please Fill All The Details Correctly!", "error")

		return;
	}
	//using ajack to request order

	$.ajax(
		{

			url: '/user/create_hotelorder',
			data: JSON.stringify({ amount: amount, date: date, hotelId: hotelId, noOfSeats: noOfSeats, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				//succes thn function call
				console.log(response)
				if (response.status == "created") {
					//open payment form
					let options = {
						key: 'rzp_test_jhw2NDpjfg1Cl9',
						amount: response.amount,
						currency: "INR",
						name: "MyTrip BOOKING",
						description: "Donation",
						image: "",
						order_id: response.id,

						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("payemt sucessfulle")

							//swal("Good job!", "Payment successfully done !", "success");

							hotelupdatePayementServer(response.razorpay_order_id, "paid");


						},

						prefill: {
							name: "",
							email: "",
							contact: ""
						},
						notes: {
							address: "Sameer DOmale"
						},
						theme: {
							color: "#3399cc"
						}

					};
					let rzp = new Razorpay(options);
					rzp.open();
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);

						swal("Failed!!", "OPPs!! Payment failed!", "error");


					});
				}
			},
			error: function(error) {
				console.log("error occur")
				alert("something went wrong !!")
			}
		}
	)

};


function hotelupdatePayementServer(orderId, status) {

	$.ajax({

		//create url to sent the request to the server with data

		url: "/user/update_hotelorder",

		// ha data ahe apla

		data: JSON.stringify({ orderId: orderId, status: status }),

		contentType: 'application/json',

		type: 'post',

		dataType: 'json',

		//after succes

		success: function(response) {

			swal("Good job!", "Payment successfully done !", "success");

		},

		error: function(error) {

			swal("Failed!", "Payment SuccesFul But We Did Not Capture Details As Sooon As!", "error");

		},

	});

}



const packagepaymentStart = () => {
	let amount = $('#add-sum').html();
	let packagelId = $('#packagelId').val();
	let noOfSeats = $('#num-one').val();
	let date = $('#nus').val();
	let address = $('#descr').val();

	if (amount == "" || amount == null || amount <= 0 || packagelId == "" || packagelId == null || noOfSeats == "" || noOfSeats == null ||
		date == "" || date == null || address == "" || address == null) {

		swal("Failed!", "Please Fill All The Details Correctly!", "error")

		return;
	}
	//using ajack to request order

	$.ajax(
		{

			url: '/user/create_packageorder',
			data: JSON.stringify({ amount: amount, address: address, date: date, packagelId: packagelId, noOfSeats: noOfSeats, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				//succes thn function call
				console.log(response)
				if (response.status == "created") {
					//open payment form
					let options = {
						key: 'rzp_test_jhw2NDpjfg1Cl9',
						amount: response.amount,
						currency: "INR",
						name: "MyTrip Booking",
						description: "Donation",
						image: "",
						order_id: response.id,

						handler: function(response) {
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(response.razorpay_signature);
							console.log("payemt sucessfulle")

							//swal("Good job!", "Payment successfully done !", "success");

							packageupdatePayementServer(response.razorpay_order_id, "paid");


						},

						prefill: {
							name: "",
							email: "",
							contact: ""
						},
						notes: {
							address: "Sameer DOmale"
						},
						theme: {
							color: "#3399cc"
						}

					};
					let rzp = new Razorpay(options);
					rzp.open();
					rzp.on('payment.failed', function(response) {
						console.log(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);

						swal("Failed!!", "OPPs!! Payment failed!", "error");


					});
				}
			},
			error: function(error) {
				console.log("error occur")
				alert("something went wrong !!")
			}
		}
	)

};


function packageupdatePayementServer(orderId, status) {

	$.ajax({

		//create url to sent the request to the server with data

		url: "/user/update_packageorder",

		// ha data ahe apla

		data: JSON.stringify({ orderId: orderId, status: status }),

		contentType: 'application/json',

		type: 'post',

		dataType: 'json',

		//after succes

		success: function(response) {

			swal("Good job!", "Payment successfully done !", "success");

		},

		error: function(error) {

			swal("Failed!", "Payment SuccesFul But We Did Not Capture Details As Sooon As!", "error");

		},

	});

}


const bussearch = () => {

	let query = $("#search-input").val();
	console.log(query);

	if (query == "") {
		$(".search-result").hide();
	}
	else {
		console.log(query);

		let url = '/user/bussearch/' + query;

		fetch(url)
			.then((response) => {
				return response.json();

			})
			.then((data) => {


				let text = "<div class='list-group' >";

				data.forEach((searchingBus) => {

					text += "sd"
				});

				text += "</div>";
				$(".search-result").html(text);
				$(".search-result").show();

			});


	}

};


