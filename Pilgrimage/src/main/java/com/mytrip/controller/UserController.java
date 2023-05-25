package com.mytrip.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mytrip.entities.Flight;
import com.mytrip.entities.FlightBook;
import com.mytrip.entities.HolidayPackageBook;
import com.mytrip.entities.Hotel;
import com.mytrip.entities.HotelBook;
import com.mytrip.entities.Package;
import com.mytrip.entities.User;
import com.mytrip.helper.Message;
import com.mytrip.service.BusBokkinService;
import com.mytrip.service.BusService;
import com.mytrip.service.EmailService;
import com.mytrip.service.FlightBookService;
import com.mytrip.service.FlightService;
import com.mytrip.service.HotelBookService;
import com.mytrip.service.HotelService;
import com.mytrip.service.PackageBookingService;
import com.mytrip.service.PackageService;
import com.mytrip.service.TrainBookingService;
import com.mytrip.service.TrainService;
import com.mytrip.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.mytrip.entities.Bus;
import com.mytrip.entities.BusBook;
import com.mytrip.entities.Email;
import com.mytrip.entities.Train;
import com.mytrip.entities.TrainBook;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private FlightService flightService;

	@Autowired
	private PackageBookingService packageBookingService;

	@Autowired
	private TrainService trainService;

	@Autowired
	private HotelBookService hotelBookService;

	@Autowired
	private BusService busService;

	@Autowired
	private PackageService packageService;

	@Autowired
	private TrainBookingService trainBookingService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private FlightBookService flightBookService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private BusBokkinService busBokkinService;

	@ModelAttribute
	public void addcommanData(Model model, Principal principal) {
		String name = principal.getName();
		User user = userService.finduserByEmail(name);
		System.out.println(user);
		model.addAttribute("user", user);
		
		List<BusBook> bookingListOfUser = busBokkinService.BookingListOfUser(user);
		
		model.addAttribute("bookingListOfUser", bookingListOfUser);
		
		
		List<TrainBook> userTrainBooking = trainBookingService.UserTrainBooking(user);
		model.addAttribute("userTrainBooking", userTrainBooking);
		
		List<FlightBook> userFlightBooking = flightBookService.UserFlightBooking(user);
		model.addAttribute("userFlightBooking", userFlightBooking);
		
		List<HotelBook> userBookingHotel = hotelBookService.UserBookingHotel(user);
		model.addAttribute("userBookingHotel", userBookingHotel);
		
		List<HolidayPackageBook> userBookingPackage = packageBookingService.UserBookingPackage(user);
		model.addAttribute("userBookingPackage", userBookingPackage);
		
		

	}

	@GetMapping("/index")
	public String UserIndex(Model model) {
		model.addAttribute("title", "Holiday-Package");
		List<Package> allPAckage = packageService.getAllPAckage();

		model.addAttribute("allPAckage", allPAckage);
		return "user/package";
	}

	@GetMapping("/flight")
	public String Flights(Model model) {
		model.addAttribute("title", "Flights");

		List<Flight> flight = flightService.getAllFlight();

		System.out.println(flight);

		model.addAttribute("flight", flight);

		return "user/flights";
	}

	@GetMapping("/hotel")
	public String Hotels(Model model) {
		model.addAttribute("title", "Hotels");

		List<Hotel> hotel = hotelService.GetAllHotel();

		model.addAttribute("hotel", hotel);

		return "user/hotels";
	}

	@GetMapping("/train")
	public String Train(Model model) {
		model.addAttribute("title", "Travel");
		List<Train> train = trainService.GetAllTrain();
		model.addAttribute("train", train);
		return "user/trains";
	}

	@GetMapping("/bus")
	public String Bus(Model model) {
		model.addAttribute("title", "Bus");
		List<Bus> bus = busService.GetAllBus();
		model.addAttribute("bus", bus);
		return "user/buses";
	}

	@PostMapping("/create_order")
	@ResponseBody
	public String creatingOrder(@RequestBody Map<String, Object> data, Model model) {
		Order order = null;
		try {

			System.out.println("oder function ex " + data);
			int amount = Integer.parseInt(data.get("amount").toString());
			int busId = Integer.parseInt(data.get("busId").toString());
			int noOfSeats = Integer.parseInt(data.get("noOfSeats").toString());
			String address = data.get("address").toString();
			String date = data.get("date").toString();

			var client = new RazorpayClient("rzp_test_jhw2NDpjfg1Cl9", "IQAW3JsE5wQnWbZX9dwoEeNv");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = client.Orders.create(orderRequest);

			BusBook busBook = new BusBook();

			busBook.setStatus("Created");
			Double amt = (double) amount;
			busBook.setBookingPrice(amt);
			busBook.setAddress(address);
			busBook.setDate(date);
			busBook.setNoOfSeats(noOfSeats);

			Bus getBusById = busService.GetBusById(busId);

			busBook.setBus(getBusById);

			User user = (User) model.getAttribute("user");

			busBook.setUser(user);

			busBook.setOrderId(order.get("id"));

			BusBook busbook2 = busBokkinService.Busbook(busBook);

			System.out.println("order : " + order);

			return order.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return order.toString();

	}

	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data, Model model) {

		System.out.println(data.get("orderId").toString());

		BusBook findbyOrderId = busBokkinService.findbyOrderId(data.get("orderId").toString());

		findbyOrderId.setStatus("Paid");

		BusBook busbook2 = busBokkinService.Busbook(findbyOrderId);

		if (busbook2 != null) {

			Email email = new Email();

			email.setTo(busbook2.getUser().getEmail());

			email.setSubject("Your Bus Book SuccessFully !!");

			String html = "<table class=\"body\" border=\"0\" align=\"center\" cellpadding=\"0\"\r\n" + "\r\n"
					+ "	cellspacing=\"0\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"center\" align=\"center\" valign=\"top\">\r\n" + "\r\n" + "	<center>\r\n" + "\r\n"
					+ "\r\n" + "\r\n" + "	<table class=\"container twelve columns\">\r\n" + "\r\n" + "	<tbody>\r\n"
					+ "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"container-content\">\r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n"
					+ "	<table class=\"twelve columns grey section-header\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"panel\">\r\n" + "\r\n" + "	<h2 class=\"text-center\">Booking Confirmation</h2>\r\n"
					+ "\r\n" + "	<h3 class=\"text-center\">\r\n" + "\r\n" + "	<small>"
					+ busbook2.getUser().getName() + "</small>\r\n" + "\r\n" + "	</h3>\r\n" + "\r\n"
					+ "	<p class=\"text-center\">\r\n" + "\r\n" + "	<strong>BUS BOOKING NUMBER - "
					+ busbook2.getOrderId() + "</strong> <br>Please\r\n" + "\r\n"
					+ "	quote this number if speaking with our customer service\r\n" + "\r\n"
					+ "	representatives.\r\n" + "\r\n" + "	</p>\r\n" + "\r\n" + "	<p>&nbsp;</p>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "\r\n" + "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n"
					+ "	<table class=\"twelve columns section-header\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"text-center\">\r\n" + "\r\n" + "	<h2 class=\"inline\">\r\n" + "\r\n"
					+ "	<strong>Bus Details</strong>\r\n" + "\r\n" + "	</h2>\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"twelve columns\">\r\n" + "\r\n"
					+ "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"two sub-columns\">\r\n" + "\r\n"
					+ "	<p>\r\n" + "\r\n" + "	<strong>" + busbook2.getBus().getCompany() + "</strong>\r\n" + "\r\n"
					+ "	</p>\r\n" + "\r\n" + "	<p class=\"sub\">" + busbook2.getBus().getBusNo() + "</p>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"ten sub-columns last\">\r\n" + "\r\n"
					+ "	<table class=\"row\">\r\n" + "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"five columns\">\r\n" + "\r\n"
					+ "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td>\r\n" + "\r\n" + "	<p>"
					+ busbook2.getBus().getDepartureCity() + "</p>\r\n" + "\r\n" + "	<p class=\"sub\">"
					+ busbook2.getBus().getDepartureTime() + "\r\n" + "\r\n" + "	</p>\r\n" + "\r\n"
					+ "	<p class=\"large\">" + busbook2.getDate() + "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n"
					+ "\r\n" + "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"five columns\">\r\n"
					+ "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td>\r\n" + "\r\n" + "	<p>"
					+ busbook2.getBus().getDestinationCity() + "</p>\r\n" + "\r\n" + "	<p class=\"sub\">"
					+ busbook2.getBus().getDestinationTime() + "</p>\r\n" + "\r\n" + "	<p class=\"large\">"
					+ busbook2.getDate() + "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n"
					+ "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	<hr> \r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n" + "	<table class=\"twelve columns grey\">\r\n"
					+ "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"text-center\">\r\n" + "\r\n" + "	<p>\r\n"
					+ "\r\n" + "	Bus Fare Rules: <a href=\"#\">Etihad Rules</a>\r\n" + "\r\n" + "	</p>\r\n"
					+ "\r\n" + "	<p>\r\n" + "\r\n" + "	MyTrip Terms & Conditions: <br />\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n" + "	<table class=\"twelve columns white\">\r\n"
					+ "\r\n" + "	<tr class=\"grey\">\r\n" + "\r\n" + "	<td></td>\r\n" + "\r\n"
					+ "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</center>\r\n"
					+ "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table> ";

			email.setMessage(html);

			emailService.sendEmail(email);

		}

		return ResponseEntity.ok(Map.of("msg", "updated"));

	}

	@PostMapping("/create_flightorder")
	@ResponseBody
	public String creatingFlightOrder(@RequestBody Map<String, Object> data, Model model) {
		Order order = null;
		try {

			System.out.println("oder function ex " + data);
			int amount = Integer.parseInt(data.get("amount").toString());
			int flightId = Integer.parseInt(data.get("flightId").toString());
			int noOfSeats = Integer.parseInt(data.get("noOfSeats").toString());

			String date = data.get("date").toString();

			var client = new RazorpayClient("rzp_test_jhw2NDpjfg1Cl9", "IQAW3JsE5wQnWbZX9dwoEeNv");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = client.Orders.create(orderRequest);

			FlightBook flightBook = new FlightBook();

			flightBook.setStatus("Created");
			Double amt = (double) amount;
			flightBook.setBookingPrice(amt);

			flightBook.setDate(date);
			flightBook.setNoOfSeats(noOfSeats);

			Flight flightById = flightService.getFlightById(flightId);

			flightBook.setFlight(flightById);

			User user = (User) model.getAttribute("user");

			flightBook.setUser(user);

			flightBook.setOrderId(order.get("id"));

			FlightBook flightBook2 = flightBookService.FlightBook(flightBook);

			System.out.println("order : " + order);

			return order.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return order.toString();

	}

	@PostMapping("/update_flightorder")
	public ResponseEntity<?> updateflightOrder(@RequestBody Map<String, Object> data, Model model) {

		System.out.println(data.get("orderId").toString());

		FlightBook findByOrderId = flightBookService.FindByOrderId(data.get("orderId").toString());

		findByOrderId.setStatus("Paid");

		FlightBook flightBook = flightBookService.FlightBook(findByOrderId);

		if (flightBook != null) {

			Email email = new Email();

			email.setTo(flightBook.getUser().getEmail());

			email.setSubject("Your Flight Book SuccessFully !!");

			String html = "<table class=\"body\" border=\"0\" align=\"center\" cellpadding=\"0\"\r\n" + "\r\n"
					+ "	cellspacing=\"0\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"center\" align=\"center\" valign=\"top\">\r\n" + "\r\n" + "	<center>\r\n" + "\r\n"
					+ "\r\n" + "\r\n" + "	<table class=\"container twelve columns\">\r\n" + "\r\n" + "	<tbody>\r\n"
					+ "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"container-content\">\r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n"
					+ "	<table class=\"twelve columns grey section-header\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"panel\">\r\n" + "\r\n" + "	<h2 class=\"text-center\">Booking Confirmation</h2>\r\n"
					+ "\r\n" + "	<h3 class=\"text-center\">\r\n" + "\r\n" + "	<small>"
					+ flightBook.getUser().getName() + "</small>\r\n" + "\r\n" + "	</h3>\r\n" + "\r\n"
					+ "	<p class=\"text-center\">\r\n" + "\r\n" + "	<strong>Flight BOOKING NUMBER - "
					+ flightBook.getOrderId() + "</strong> <br>Please\r\n" + "\r\n"
					+ "	quote this number if speaking with our customer service\r\n" + "\r\n"
					+ "	representatives.\r\n" + "\r\n" + "	</p>\r\n" + "\r\n" + "	<p>&nbsp;</p>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "\r\n" + "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n"
					+ "	<table class=\"twelve columns section-header\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"text-center\">\r\n" + "\r\n" + "	<h2 class=\"inline\">\r\n" + "\r\n"
					+ "	<strong>Flight Details</strong>\r\n" + "\r\n" + "	</h2>\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"twelve columns\">\r\n" + "\r\n"
					+ "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"two sub-columns\">\r\n" + "\r\n"
					+ "	<p>\r\n" + "\r\n" + "	<strong>" + flightBook.getFlight().getCompany() + "</strong>\r\n"
					+ "\r\n" + "	</p>\r\n" + "\r\n" + "	<p class=\"sub\">" + flightBook.getFlight().getFlightNo()
					+ "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	<td class=\"ten sub-columns last\">\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n"
					+ "\r\n" + "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"five columns\">\r\n"
					+ "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td>\r\n" + "\r\n" + "	<p>"
					+ flightBook.getFlight().getDepartureCity() + "</p>\r\n" + "\r\n" + "	<p class=\"sub\">"
					+ flightBook.getFlight().getDepartureTime() + "\r\n" + "\r\n" + "	</p>\r\n" + "\r\n"
					+ "	<p class=\"large\">" + flightBook.getDate() + "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n"
					+ "\r\n" + "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"five columns\">\r\n"
					+ "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td>\r\n" + "\r\n" + "	<p>"
					+ flightBook.getFlight().getDestinationCity() + "</p>\r\n" + "\r\n" + "	<p class=\"sub\">"
					+ flightBook.getFlight().getDestinationTime() + "</p>\r\n" + "\r\n" + "	<p class=\"large\">"
					+ flightBook.getDate() + "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n"
					+ "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	<hr> \r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n" + "	<table class=\"twelve columns grey\">\r\n"
					+ "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"text-center\">\r\n" + "\r\n" + "	<p>\r\n"
					+ "\r\n" + "	Flight Fare Rules: <a href=\"#\">Etihad Rules</a>\r\n" + "\r\n" + "	</p>\r\n"
					+ "\r\n" + "	<p>\r\n" + "\r\n" + "	MyTrip Terms & Conditions: <br />\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n" + "	<table class=\"twelve columns white\">\r\n"
					+ "\r\n" + "	<tr class=\"grey\">\r\n" + "\r\n" + "	<td></td>\r\n" + "\r\n"
					+ "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</center>\r\n"
					+ "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table> ";

			email.setMessage(html);

			emailService.sendEmail(email);

		}

		return ResponseEntity.ok(Map.of("msg", "updated"));

	}

	@PostMapping("/create_traintorder")
	@ResponseBody
	public String creatingTrainOrder(@RequestBody Map<String, Object> data, Model model) {
		Order order = null;
		try {

			System.out.println("oder function ex " + data);
			int amount = Integer.parseInt(data.get("amount").toString());
			int trainId = Integer.parseInt(data.get("trainId").toString());
			int noOfSeats = Integer.parseInt(data.get("noOfSeats").toString());

			String date = data.get("date").toString();

			var client = new RazorpayClient("rzp_test_jhw2NDpjfg1Cl9", "IQAW3JsE5wQnWbZX9dwoEeNv");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = client.Orders.create(orderRequest);

			TrainBook trainBook = new TrainBook();

			trainBook.setStatus("Created");
			Double amt = (double) amount;
			trainBook.setBookingPrice(amt);

			trainBook.setDate(date);
			trainBook.setNoOfSeats(noOfSeats);

			Train trainById = trainService.getTrainById(trainId);

			trainBook.setTrain(trainById);

			User user = (User) model.getAttribute("user");

			trainBook.setUser(user);

			trainBook.setOrderId(order.get("id"));

			TrainBook trainBook1 = trainBookingService.TrainBook(trainBook);

			System.out.println("order : " + order);

			return order.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return order.toString();

	}

	@PostMapping("/update_trainorder")
	public ResponseEntity<?> updatetrainOrder(@RequestBody Map<String, Object> data, Model model) {

		System.out.println(data.get("orderId").toString());

		TrainBook findByOrderId = trainBookingService.FindByOrderId(data.get("orderId").toString());

		findByOrderId.setStatus("Paid");

		TrainBook trainBook = trainBookingService.TrainBook(findByOrderId);

		if (trainBook != null) {

			Email email = new Email();

			email.setTo(trainBook.getUser().getEmail());

			email.setSubject("Your Train Book SuccessFully !!");

			String html = "<table class=\"body\" border=\"0\" align=\"center\" cellpadding=\"0\"\r\n" + "\r\n"
					+ "	cellspacing=\"0\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"center\" align=\"center\" valign=\"top\">\r\n" + "\r\n" + "	<center>\r\n" + "\r\n"
					+ "\r\n" + "\r\n" + "	<table class=\"container twelve columns\">\r\n" + "\r\n" + "	<tbody>\r\n"
					+ "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"container-content\">\r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n"
					+ "	<table class=\"twelve columns grey section-header\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"panel\">\r\n" + "\r\n" + "	<h2 class=\"text-center\">Booking Confirmation</h2>\r\n"
					+ "\r\n" + "	<h3 class=\"text-center\">\r\n" + "\r\n" + "	<small>"
					+ trainBook.getUser().getName() + "</small>\r\n" + "\r\n" + "	</h3>\r\n" + "\r\n"
					+ "	<p class=\"text-center\">\r\n" + "\r\n" + "	<strong>TRAIN BOOKING NUMBER - "
					+ trainBook.getOrderId() + "</strong> <br>Please\r\n" + "\r\n"
					+ "	quote this number if speaking with our customer service\r\n" + "\r\n"
					+ "	representatives.\r\n" + "\r\n" + "	</p>\r\n" + "\r\n" + "	<p>&nbsp;</p>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "\r\n" + "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n"
					+ "	<table class=\"twelve columns section-header\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"text-center\">\r\n" + "\r\n" + "	<h2 class=\"inline\">\r\n" + "\r\n"
					+ "	<strong>TRAIN Details</strong>\r\n" + "\r\n" + "	</h2>\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"twelve columns\">\r\n" + "\r\n"
					+ "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"two sub-columns\">\r\n" + "\r\n"
					+ "	<p>\r\n" + "\r\n" + "	<strong>" + trainBook.getTrain().getCompany() + "</strong>\r\n" + "\r\n"
					+ "	</p>\r\n" + "\r\n" + "	<p class=\"sub\">" + trainBook.getTrain().getTrainNo() + "</p>\r\n"
					+ "\r\n" + "	</td>\r\n" + "\r\n" + "	<td class=\"ten sub-columns last\">\r\n" + "\r\n"
					+ "	<table class=\"row\">\r\n" + "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"five columns\">\r\n" + "\r\n"
					+ "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td>\r\n" + "\r\n" + "	<p>"
					+ trainBook.getTrain().getDepartureCity() + "</p>\r\n" + "\r\n" + "	<p class=\"sub\">"
					+ trainBook.getTrain().getDepartureTime() + "\r\n" + "\r\n" + "	</p>\r\n" + "\r\n"
					+ "	<p class=\"large\">" + trainBook.getDate() + "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n"
					+ "\r\n" + "	<td class=\"wrapper\">\r\n" + "\r\n" + "	<table class=\"five columns\">\r\n"
					+ "\r\n" + "	<tbody>\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td>\r\n" + "\r\n" + "	<p>"
					+ trainBook.getTrain().getDestinationCity() + "</p>\r\n" + "\r\n" + "	<p class=\"sub\">"
					+ trainBook.getTrain().getDestinationTime() + "</p>\r\n" + "\r\n" + "	<p class=\"large\">"
					+ trainBook.getDate() + "</p>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n"
					+ "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	<hr> \r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n" + "	<table class=\"twelve columns grey\">\r\n"
					+ "\r\n" + "	<tr>\r\n" + "\r\n" + "	<td class=\"text-center\">\r\n" + "\r\n" + "	<p>\r\n"
					+ "\r\n" + "	Train Fare Rules: <a href=\"#\">Etihad Rules</a>\r\n" + "\r\n" + "	</p>\r\n"
					+ "\r\n" + "	<p>\r\n" + "\r\n" + "	MyTrip Terms & Conditions: <br />\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n"
					+ "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n"
					+ "\r\n" + "	<table class=\"row\">\r\n" + "\r\n" + "	<tr>\r\n" + "\r\n"
					+ "	<td class=\"wrapper last\">\r\n" + "\r\n" + "	<table class=\"twelve columns white\">\r\n"
					+ "\r\n" + "	<tr class=\"grey\">\r\n" + "\r\n" + "	<td></td>\r\n" + "\r\n"
					+ "	<td class=\"expander\"></td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n"
					+ "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</td>\r\n" + "\r\n"
					+ "	</tr>\r\n" + "\r\n" + "	</tbody>\r\n" + "\r\n" + "	</table>\r\n" + "\r\n" + "	</center>\r\n"
					+ "\r\n" + "	</td>\r\n" + "\r\n" + "	</tr>\r\n" + "\r\n" + "	</table> ";

			email.setMessage(html);

			emailService.sendEmail(email);

		}

		return ResponseEntity.ok(Map.of("msg", "updated"));

	}

	@PostMapping("/create_hotelorder")
	@ResponseBody
	public String creatingHotelOrder(@RequestBody Map<String, Object> data, Model model) {
		Order order = null;
		try {

			System.out.println("oder function ex " + data);
			int amount = Integer.parseInt(data.get("amount").toString());
			int hotelId = Integer.parseInt(data.get("hotelId").toString());
			int noOfSeats = Integer.parseInt(data.get("noOfSeats").toString());

			String date = data.get("date").toString();

			var client = new RazorpayClient("rzp_test_jhw2NDpjfg1Cl9", "IQAW3JsE5wQnWbZX9dwoEeNv");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = client.Orders.create(orderRequest);

			HotelBook hotelBook = new HotelBook();

			hotelBook.setStatus("Created");
			Double amt = (double) amount;
			hotelBook.setBookingPrice(amt);

			hotelBook.setDate(date);
			hotelBook.setNoOfPerson(noOfSeats);

			Hotel hotelByID = hotelService.HotelByID(hotelId);

			hotelBook.setHotel(hotelByID);

			User user = (User) model.getAttribute("user");

			hotelBook.setUser(user);

			hotelBook.setOrderId(order.get("id"));

			HotelBook hotelBoook = hotelBookService.HotelBoook(hotelBook);

			System.out.println("order : " + order);

			return order.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return order.toString();

	}

	@PostMapping("/update_hotelorder")
	public ResponseEntity<?> updateHotelOrder(@RequestBody Map<String, Object> data, Model model) {

		System.out.println(data.get("orderId").toString());

		HotelBook findByOrderId = hotelBookService.FindByOrderId(data.get("orderId").toString());

		findByOrderId.setStatus("Paid");

		HotelBook hotelBoook = hotelBookService.HotelBoook(findByOrderId);

		if (hotelBoook != null) {

			Email email = new Email();

			email.setTo(hotelBoook.getUser().getEmail());

			email.setSubject("Your Hotel Book SuccessFully !!");

			String html = "<!doctype html>\r\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "\r\n"
					+ "<head>\r\n" + "<meta name=\"viewport\" content=\"width=device-width\">\r\n"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n"
					+ "<!-- Turn off iOS phone number autodetect -->\r\n"
					+ "<meta name=\"format-detection\" content=\"telephone=no\">\r\n" + "<style>\r\n" + "body, p {\r\n"
					+ "	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\r\n"
					+ "	-webkit-font-smoothing: antialiased;\r\n" + "	-webkit-text-size-adjust: none;\r\n" + "}\r\n"
					+ "\r\n" + "table {\r\n" + "	border-collapse: collapse;\r\n" + "	border-spacing: 0;\r\n"
					+ "	border: 0;\r\n" + "	padding: 0;\r\n" + "}\r\n" + "\r\n" + "img {\r\n" + "	margin: 0;\r\n"
					+ "	padding: 0;\r\n" + "}\r\n" + "\r\n" + ".content {\r\n" + "	width: 600px;\r\n" + "}\r\n"
					+ "\r\n" + ".no_text_resize {\r\n" + "	-moz-text-size-adjust: none;\r\n"
					+ "	-webkit-text-size-adjust: none;\r\n" + "	-ms-text-size-adjust: none;\r\n"
					+ "	text-size-adjust: none;\r\n" + "}\r\n" + "\r\n" + "/* Media Queries */\r\n"
					+ "@media all and (max-width: 600px) {\r\n" + "	table[class=\"content\"] {\r\n"
					+ "		width: 100% !important;\r\n" + "	}\r\n"
					+ "	tr[class=\"grid-no-gutter\"] td[class=\"grid__col\"] {\r\n"
					+ "		padding-left: 0 !important;\r\n" + "		padding-right: 0 !important;\r\n" + "	}\r\n"
					+ "	td[class=\"grid__col\"] {\r\n" + "		padding-left: 18px !important;\r\n"
					+ "		padding-right: 18px !important;\r\n" + "	}\r\n"
					+ "	table[class=\"small_full_width\"] {\r\n" + "		width: 100% !important;\r\n"
					+ "		padding-bottom: 10px;\r\n" + "	}\r\n" + "	a[class=\"header-link\"] {\r\n"
					+ "		margin-right: 0 !important;\r\n" + "		margin-left: 10px !important;\r\n" + "	}\r\n"
					+ "	a[class=\"btn\"] {\r\n" + "		width: 100%;\r\n"
					+ "		border-left-width: 0px !important;\r\n" + "		border-right-width: 0px !important;\r\n"
					+ "	}\r\n" + "	table[class=\"col-layout\"] {\r\n" + "		width: 100% !important;\r\n"
					+ "	}\r\n" + "	td[class=\"col-container\"] {\r\n" + "		display: block !important;\r\n"
					+ "		width: 100% !important;\r\n" + "		padding-left: 0 !important;\r\n"
					+ "		padding-right: 0 !important;\r\n" + "	}\r\n" + "	td[class=\"col-nav-items\"] {\r\n"
					+ "		display: inline-block !important;\r\n" + "		padding-left: 0 !important;\r\n"
					+ "		padding-right: 10px !important;\r\n" + "		background: none !important;\r\n"
					+ "	}\r\n" + "	img[class=\"col-img\"] {\r\n" + "		height: auto !important;\r\n"
					+ "		max-width: 520px !important;\r\n" + "		width: 100% !important;\r\n" + "	}\r\n"
					+ "	td[class=\"col-center-sm\"] {\r\n" + "		text-align: center;\r\n" + "	}\r\n"
					+ "	tr[class=\"footer-attendee-cta\"]>td[class=\"grid__col\"] {\r\n"
					+ "		padding: 24px 0 0 !important;\r\n" + "	}\r\n" + "	td[class=\"col-footer-cta\"] {\r\n"
					+ "		padding-left: 0 !important;\r\n" + "		padding-right: 0 !important;\r\n" + "	}\r\n"
					+ "	td[class=\"footer-links\"] {\r\n" + "		text-align: left !important;\r\n" + "	}\r\n"
					+ "	.hide-for-small {\r\n" + "		display: none !important;\r\n" + "	}\r\n"
					+ "	.ribbon-mobile {\r\n" + "		line-height: 1.3 !important;\r\n" + "	}\r\n"
					+ "	.small_full_width {\r\n" + "		width: 100% !important;\r\n"
					+ "		padding-bottom: 10px;\r\n" + "	}\r\n" + "	.table__ridge {\r\n"
					+ "		height: 7px !important;\r\n" + "	}\r\n" + "	.table__ridge img {\r\n"
					+ "		display: none !important;\r\n" + "	}\r\n" + "	.table__ridge--top {\r\n"
					+ "		background-image:\r\n"
					+ "			url(https://cdn.evbstatic.com/s3-s3/marketing/emails/modules/ridges_top_fullx2.jpg)\r\n"
					+ "			!important;\r\n" + "		background-size: 170% 7px;\r\n" + "	}\r\n"
					+ "	.table__ridge--bottom {\r\n" + "		background-image:\r\n"
					+ "			url(https://cdn.evbstatic.com/s3-s3/marketing/emails/modules/ridges_bottom_fullx2.jpg)\r\n"
					+ "			!important;\r\n" + "		background-size: 170% 7px;\r\n" + "	}\r\n"
					+ "	.summary-table__total {\r\n" + "		padding-right: 10px !important;\r\n" + "	}\r\n"
					+ "	.app-cta {\r\n" + "		display: none !important;\r\n" + "	}\r\n" + "	.app-cta__mobile {\r\n"
					+ "		width: 100% !important;\r\n" + "		height: auto !important;\r\n"
					+ "		max-height: none !important;\r\n" + "		overflow: visible !important;\r\n"
					+ "		float: none !important;\r\n" + "		display: block !important;\r\n"
					+ "		margin-top: 12px !important;\r\n" + "		visibility: visible;\r\n"
					+ "		font-size: inherit !important;\r\n" + "	}\r\n" + "\r\n" + "	/* List Event Cards */\r\n"
					+ "	.list-card__header {\r\n" + "		width: 130px !important;\r\n" + "	}\r\n"
					+ "	.list-card__label {\r\n" + "		width: 130px !important;\r\n" + "	}\r\n"
					+ "	.list-card__image-wrapper {\r\n" + "		width: 130px !important;\r\n"
					+ "		height: 65px !important;\r\n" + "	}\r\n" + "	.list-card__image {\r\n"
					+ "		max-width: 130px !important;\r\n" + "		max-height: 65px !important;\r\n" + "	}\r\n"
					+ "	.list-card__body {\r\n" + "		padding-left: 10px !important;\r\n" + "	}\r\n"
					+ "	.list-card__title {\r\n" + "		margin-bottom: 10px !important;\r\n" + "	}\r\n"
					+ "	.list-card__date {\r\n" + "		padding-top: 0 !important;\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
					+ "@media all and (device-width: 768px) and (device-height: 1024px) and\r\n"
					+ "	(orientation:landscape) {\r\n" + "	.ribbon-mobile {\r\n"
					+ "		line-height: 1.3 !important;\r\n" + "	}\r\n" + "	.ribbon-mobile__text {\r\n"
					+ "		padding: 0 !important;\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
					+ "@media all and (device-width: 768px) and (device-height: 1024px) and\r\n"
					+ "	(orientation:portrait) {\r\n" + "	.ribbon-mobile {\r\n"
					+ "		line-height: 1.3 !important;\r\n" + "	}\r\n" + "	.ribbon-mobile__text {\r\n"
					+ "		padding: 0 !important;\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
					+ "@media screen and (min-device-height:480px) and (max-device-height:568px) , (\r\n"
					+ "		min-device-width : 375px) and (max-device-width : 667px) and\r\n"
					+ "		(-webkit-min-device-pixel-ratio : 2) , ( min-device-width : 414px) and\r\n"
					+ "	(max-device-width : 736px) and (-webkit-min-device-pixel-ratio : 3) {\r\n"
					+ "	.hide_for_iphone {\r\n" + "		display: none !important;\r\n" + "	}\r\n" + "	.passbook {\r\n"
					+ "		width: auto !important;\r\n" + "		height: auto !important;\r\n"
					+ "		line-height: auto !important;\r\n" + "		visibility: visible !important;\r\n"
					+ "		display: block !important;\r\n" + "		max-height: none !important;\r\n"
					+ "		overflow: visible !important;\r\n" + "		float: none !important;\r\n"
					+ "		text-indent: 0 !important;\r\n" + "		font-size: inherit !important;\r\n" + "	}\r\n"
					+ "}\r\n" + "</style>\r\n" + "</head>\r\n" + "\r\n" + "\r\n"
					+ "<body border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\"\r\n"
					+ "	width=\"100%\" bgcolor=\"#F7F7F7\" style=\"margin: 0;\">\r\n"
					+ "	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\"\r\n"
					+ "		width=\"100%\" bgcolor=\"#F7F7F7\">\r\n" + "		<tr>\r\n"
					+ "			<td style=\"padding-right: 10px; padding-left: 10px;\">\r\n" + "			\r\n"
					+ "				\r\n" + "                </td>\r\n" + "              </tr>\r\n"
					+ "            </table>\r\n" + "          <![endif]-->\r\n" + "			</td>\r\n"
					+ "		</tr>\r\n" + "		<tr>\r\n" + "			<td>\r\n" + "				\r\n"
					+ "				<table class=\"content\" align=\"center\" cellpadding=\"0\"\r\n"
					+ "					cellspacing=\"0\" border=\"0\" bgcolor=\"#F7F7F7\"\r\n"
					+ "					style=\"width: 600px; max-width: 600px;\">\r\n" + "					<tr>\r\n"
					+ "						<td colspan=\"2\" style=\"background: #fff; border-radius: 8px;\">\r\n"
					+ "							<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
					+ "								<tr>\r\n" + "									<td\r\n"
					+ "										style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\">\r\n"
					+ "								<tr class=\"\">\r\n"
					+ "									<td class=\"grid__col\"\r\n"
					+ "										style=\"font-family: 'Helvetica neue', Helvetica, arial, sans-serif; padding: 32px 40px;\">\r\n"
					+ "\r\n" + "										<h2\r\n"
					+ "											style=\"color: #404040; font-weight: 300; margin: 0 0 12px 0; font-size: 20px; line-height: 30px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\">\r\n"
					+ "\r\n" + "											Hi {" + hotelBoook.getUser().getName()
					+ "},</h2>\r\n" + "\r\n" + "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 15px; line-height: 21px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\"\r\n"
					+ "											class=\"\">Your reservation request for {{"
					+ hotelBoook.getHotel().getHotelName() + "}},\r\n" + "											{{"
					+ hotelBoook.getHotel().getLocation() + "}} has been confirmed. Please review the details of\r\n"
					+ "											your booking.</p>\r\n" + "\r\n"
					+ "										<table width=\"100%\" border=\"2\" cellspacing=\"0\" cellpadding=\"0\"\r\n"
					+ "											style=\"margin-top: 12px; margin-bottom: 12px; margin: 24px 0; color: #666666; font-weight: 400; font-size: 15px; line-height: 21px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\">\r\n"
					+ "											<tr>\r\n"
					+ "												<td\r\n"
					+ "													style=\"padding: 20px 20px 0px; font-weight: 700; font-size: 25px;\">\r\n"
					+ "													\r\n"
					+ "												<p\r\n"
					+ "														style=\"padding-top: -5px; font-weight: 700; font-size: 12px;\">Booking\r\n"
					+ "														Confirmation Code: {{"
					+ hotelBoook.getOrderId() + "}}</p>\r\n" + "\r\n"
					+ "												</td>\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												<td\r\n"
					+ "													style=\"padding: 20px 20px 10px; font-weight: 700; font-size: 18px;\">{{"
					+ hotelBoook.getHotel().getHotelName() + "}}\r\n"
					+ "													<p\r\n"
					+ "														style=\"padding-top: 0px; font-weight: 700; font-size: 12px;\">{{Total\r\n"
					+ "														Rooms: "
					+ hotelBoook.getHotel().getNoofRooms() + "}} {{Room Type Name : " + hotelBoook.getHotel().getType()
					+ "}} - {{Total Guests : " + hotelBoook.getHotel().getNoofPerson() + "}}</p>\r\n"
					+ "												</td>\r\n" + "\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												<td></td>\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												<td>\r\n" + "\r\n"
					+ "													<table style=\"width: 100%;!important\">\r\n"
					+ "\r\n" + "														\r\n"
					+ "														\r\n"
					+ "														\r\n" + "\r\n"
					+ "														\r\n"
					+ "														\r\n"
					+ "														<tr>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 20px; font-weight: 700; font-size: 14px; color: #000\">Grand\r\n"
					+ "																Total</td>\r\n"
					+ "															<td></td>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 30px; font-weight: 700; font-size: 14px; color: #000;\">₹\r\n"
					+ "																" + hotelBoook.getBookingPrice()
					+ "</td>\r\n" + "														</tr>\r\n"
					+ "														<tr>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 20px; font-weight: 700; font-size: 14px;\">Payment\r\n"
					+ "																Mode</td>\r\n"
					+ "															<td></td>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 30px; font-weight: 700; font-size: 14px;\">Pay\r\n"
					+ "																@ Online</td>\r\n"
					+ "														</tr>\r\n" + "\r\n" + "\r\n"
					+ "														</tr>\r\n" + "\r\n" + "\r\n"
					+ "													</table>\r\n" + "\r\n" + "\r\n"
					+ "												</td>\r\n"
					+ "											<tr>\r\n"
					+ "												<td\r\n"
					+ "													style=\"padding: 20px 20px 10px; font-weight: 700; font-size: 18px;\">Details\r\n"
					+ "													<p\r\n"
					+ "														style=\"padding-top: 0px; font-weight: 700; font-size: 12px;\">\r\n"
					+ "													<ul\r\n"
					+ "														style=\"padding-top: 0px; font-weight: 300; font-size: 14px;\">\r\n"
					+ "														<li>"
					+ hotelBoook.getHotel().getDescription() + "</li>\r\n"
					+ "														\r\n"
					+ "													</ul>\r\n"
					+ "													</p>\r\n"
					+ "												</td>\r\n" + "\r\n"
					+ "											</tr>\r\n" + "\r\n" + "\r\n"
					+ "											<td></td>\r\n" + "\r\n" + "\r\n"
					+ "											</tr>\r\n" + "\r\n"
					+ "											</tr>\r\n"
					+ "										</table> \r\n"
					+ "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 15px; line-height: 21px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\"\r\n"
					+ "											class=\"\">Hope you enjoyed the booking experience and will\r\n"
					+ "											like the stay too.</p>\r\n"
					+ "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 17px; line-height: 24px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif; margin-bottom: 6px; margin-top: 24px;\"\r\n"
					+ "											class=\"\">Cheers,</p>\r\n"
					+ "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 17px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif; margin-bottom: 6px; margin-top: 10px;\">MyTrip\r\n"
					+ "											Direct Team</p>\r\n"
					+ "									</td>\r\n" + "								</tr>\r\n"
					+ "								</td>\r\n" + "								</tr>\r\n"
					+ "							</table>\r\n" + "						</td>\r\n"
					+ "					</tr>\r\n" + "				</table> \r\n" + "			</td>\r\n"
					+ "		</tr>\r\n" + "	</table>\r\n" + "</body>\r\n" + "\r\n" + "</html>";

			email.setMessage(html);

			emailService.sendEmail(email);

		}

		return ResponseEntity.ok(Map.of("msg", "updated"));

	}

	@PostMapping("/create_packageorder")
	@ResponseBody
	public String creatingPackageOrder(@RequestBody Map<String, Object> data, Model model) {
		Order order = null;
		try {

			System.out.println("oder function ex " + data);
			int amount = Integer.parseInt(data.get("amount").toString());
			int packagelId = Integer.parseInt(data.get("packagelId").toString());
			int noOfSeats = Integer.parseInt(data.get("noOfSeats").toString());
			String address = data.get("address").toString();
			String date = data.get("date").toString();

			var client = new RazorpayClient("rzp_test_jhw2NDpjfg1Cl9", "IQAW3JsE5wQnWbZX9dwoEeNv");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = client.Orders.create(orderRequest);

			HolidayPackageBook holidaybook = new HolidayPackageBook();

			holidaybook.setStatus("Created");
			Double amt = (double) amount;
			holidaybook.setBookingPrice(amt);

			holidaybook.setDate(date);
			holidaybook.setNoOfPerson(noOfSeats);

			holidaybook.setAddress(address);

			Package singlePackage = packageService.getSinglePackage(packagelId);

			holidaybook.setPackage1(singlePackage);

			User user = (User) model.getAttribute("user");

			holidaybook.setUser(user);

			holidaybook.setOrderId(order.get("id"));

			HolidayPackageBook bookPackage = packageBookingService.BookPackage(holidaybook);

			System.out.println("order : " + order);

			return order.toString();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return order.toString();

	}

	@PostMapping("/update_packageorder")
	public ResponseEntity<?> updatePackageOrder(@RequestBody Map<String, Object> data, Model model) {

		System.out.println(data.get("orderId").toString());

		HolidayPackageBook findByOrderId = packageBookingService.FindByOrderId(data.get("orderId").toString());

		findByOrderId.setStatus("Paid");

		HolidayPackageBook bookPackage = packageBookingService.BookPackage(findByOrderId);

		if (bookPackage != null) {

			Email email = new Email();

			email.setTo(bookPackage.getUser().getEmail());

			email.setSubject("Your Holiday-Package Book SuccessFully !!");

			String html = "<!doctype html>\r\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "\r\n"
					+ "<head>\r\n" + "<meta name=\"viewport\" content=\"width=device-width\">\r\n"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n"
					+ "<!-- Turn off iOS phone number autodetect -->\r\n"
					+ "<meta name=\"format-detection\" content=\"telephone=no\">\r\n" + "<style>\r\n" + "body, p {\r\n"
					+ "	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\r\n"
					+ "	-webkit-font-smoothing: antialiased;\r\n" + "	-webkit-text-size-adjust: none;\r\n" + "}\r\n"
					+ "\r\n" + "table {\r\n" + "	border-collapse: collapse;\r\n" + "	border-spacing: 0;\r\n"
					+ "	border: 0;\r\n" + "	padding: 0;\r\n" + "}\r\n" + "\r\n" + "img {\r\n" + "	margin: 0;\r\n"
					+ "	padding: 0;\r\n" + "}\r\n" + "\r\n" + ".content {\r\n" + "	width: 600px;\r\n" + "}\r\n"
					+ "\r\n" + ".no_text_resize {\r\n" + "	-moz-text-size-adjust: none;\r\n"
					+ "	-webkit-text-size-adjust: none;\r\n" + "	-ms-text-size-adjust: none;\r\n"
					+ "	text-size-adjust: none;\r\n" + "}\r\n" + "\r\n" + "/* Media Queries */\r\n"
					+ "@media all and (max-width: 600px) {\r\n" + "	table[class=\"content\"] {\r\n"
					+ "		width: 100% !important;\r\n" + "	}\r\n"
					+ "	tr[class=\"grid-no-gutter\"] td[class=\"grid__col\"] {\r\n"
					+ "		padding-left: 0 !important;\r\n" + "		padding-right: 0 !important;\r\n" + "	}\r\n"
					+ "	td[class=\"grid__col\"] {\r\n" + "		padding-left: 18px !important;\r\n"
					+ "		padding-right: 18px !important;\r\n" + "	}\r\n"
					+ "	table[class=\"small_full_width\"] {\r\n" + "		width: 100% !important;\r\n"
					+ "		padding-bottom: 10px;\r\n" + "	}\r\n" + "	a[class=\"header-link\"] {\r\n"
					+ "		margin-right: 0 !important;\r\n" + "		margin-left: 10px !important;\r\n" + "	}\r\n"
					+ "	a[class=\"btn\"] {\r\n" + "		width: 100%;\r\n"
					+ "		border-left-width: 0px !important;\r\n" + "		border-right-width: 0px !important;\r\n"
					+ "	}\r\n" + "	table[class=\"col-layout\"] {\r\n" + "		width: 100% !important;\r\n"
					+ "	}\r\n" + "	td[class=\"col-container\"] {\r\n" + "		display: block !important;\r\n"
					+ "		width: 100% !important;\r\n" + "		padding-left: 0 !important;\r\n"
					+ "		padding-right: 0 !important;\r\n" + "	}\r\n" + "	td[class=\"col-nav-items\"] {\r\n"
					+ "		display: inline-block !important;\r\n" + "		padding-left: 0 !important;\r\n"
					+ "		padding-right: 10px !important;\r\n" + "		background: none !important;\r\n"
					+ "	}\r\n" + "	img[class=\"col-img\"] {\r\n" + "		height: auto !important;\r\n"
					+ "		max-width: 520px !important;\r\n" + "		width: 100% !important;\r\n" + "	}\r\n"
					+ "	td[class=\"col-center-sm\"] {\r\n" + "		text-align: center;\r\n" + "	}\r\n"
					+ "	tr[class=\"footer-attendee-cta\"]>td[class=\"grid__col\"] {\r\n"
					+ "		padding: 24px 0 0 !important;\r\n" + "	}\r\n" + "	td[class=\"col-footer-cta\"] {\r\n"
					+ "		padding-left: 0 !important;\r\n" + "		padding-right: 0 !important;\r\n" + "	}\r\n"
					+ "	td[class=\"footer-links\"] {\r\n" + "		text-align: left !important;\r\n" + "	}\r\n"
					+ "	.hide-for-small {\r\n" + "		display: none !important;\r\n" + "	}\r\n"
					+ "	.ribbon-mobile {\r\n" + "		line-height: 1.3 !important;\r\n" + "	}\r\n"
					+ "	.small_full_width {\r\n" + "		width: 100% !important;\r\n"
					+ "		padding-bottom: 10px;\r\n" + "	}\r\n" + "	.table__ridge {\r\n"
					+ "		height: 7px !important;\r\n" + "	}\r\n" + "	.table__ridge img {\r\n"
					+ "		display: none !important;\r\n" + "	}\r\n" + "	.table__ridge--top {\r\n"
					+ "		background-image:\r\n"
					+ "			url(https://cdn.evbstatic.com/s3-s3/marketing/emails/modules/ridges_top_fullx2.jpg)\r\n"
					+ "			!important;\r\n" + "		background-size: 170% 7px;\r\n" + "	}\r\n"
					+ "	.table__ridge--bottom {\r\n" + "		background-image:\r\n"
					+ "			url(https://cdn.evbstatic.com/s3-s3/marketing/emails/modules/ridges_bottom_fullx2.jpg)\r\n"
					+ "			!important;\r\n" + "		background-size: 170% 7px;\r\n" + "	}\r\n"
					+ "	.summary-table__total {\r\n" + "		padding-right: 10px !important;\r\n" + "	}\r\n"
					+ "	.app-cta {\r\n" + "		display: none !important;\r\n" + "	}\r\n" + "	.app-cta__mobile {\r\n"
					+ "		width: 100% !important;\r\n" + "		height: auto !important;\r\n"
					+ "		max-height: none !important;\r\n" + "		overflow: visible !important;\r\n"
					+ "		float: none !important;\r\n" + "		display: block !important;\r\n"
					+ "		margin-top: 12px !important;\r\n" + "		visibility: visible;\r\n"
					+ "		font-size: inherit !important;\r\n" + "	}\r\n" + "\r\n" + "	/* List Event Cards */\r\n"
					+ "	.list-card__header {\r\n" + "		width: 130px !important;\r\n" + "	}\r\n"
					+ "	.list-card__label {\r\n" + "		width: 130px !important;\r\n" + "	}\r\n"
					+ "	.list-card__image-wrapper {\r\n" + "		width: 130px !important;\r\n"
					+ "		height: 65px !important;\r\n" + "	}\r\n" + "	.list-card__image {\r\n"
					+ "		max-width: 130px !important;\r\n" + "		max-height: 65px !important;\r\n" + "	}\r\n"
					+ "	.list-card__body {\r\n" + "		padding-left: 10px !important;\r\n" + "	}\r\n"
					+ "	.list-card__title {\r\n" + "		margin-bottom: 10px !important;\r\n" + "	}\r\n"
					+ "	.list-card__date {\r\n" + "		padding-top: 0 !important;\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
					+ "@media all and (device-width: 768px) and (device-height: 1024px) and\r\n"
					+ "	(orientation:landscape) {\r\n" + "	.ribbon-mobile {\r\n"
					+ "		line-height: 1.3 !important;\r\n" + "	}\r\n" + "	.ribbon-mobile__text {\r\n"
					+ "		padding: 0 !important;\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
					+ "@media all and (device-width: 768px) and (device-height: 1024px) and\r\n"
					+ "	(orientation:portrait) {\r\n" + "	.ribbon-mobile {\r\n"
					+ "		line-height: 1.3 !important;\r\n" + "	}\r\n" + "	.ribbon-mobile__text {\r\n"
					+ "		padding: 0 !important;\r\n" + "	}\r\n" + "}\r\n" + "\r\n"
					+ "@media screen and (min-device-height:480px) and (max-device-height:568px) , (\r\n"
					+ "		min-device-width : 375px) and (max-device-width : 667px) and\r\n"
					+ "		(-webkit-min-device-pixel-ratio : 2) , ( min-device-width : 414px) and\r\n"
					+ "	(max-device-width : 736px) and (-webkit-min-device-pixel-ratio : 3) {\r\n"
					+ "	.hide_for_iphone {\r\n" + "		display: none !important;\r\n" + "	}\r\n" + "	.passbook {\r\n"
					+ "		width: auto !important;\r\n" + "		height: auto !important;\r\n"
					+ "		line-height: auto !important;\r\n" + "		visibility: visible !important;\r\n"
					+ "		display: block !important;\r\n" + "		max-height: none !important;\r\n"
					+ "		overflow: visible !important;\r\n" + "		float: none !important;\r\n"
					+ "		text-indent: 0 !important;\r\n" + "		font-size: inherit !important;\r\n" + "	}\r\n"
					+ "}\r\n" + "</style>\r\n" + "</head>\r\n" + "\r\n" + "\r\n"
					+ "<body border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\"\r\n"
					+ "	width=\"100%\" bgcolor=\"#F7F7F7\" style=\"margin: 0;\">\r\n"
					+ "	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\"\r\n"
					+ "		width=\"100%\" bgcolor=\"#F7F7F7\">\r\n" + "		<tr>\r\n"
					+ "			<td style=\"padding-right: 10px; padding-left: 10px;\">\r\n" + "			\r\n"
					+ "				\r\n" + "                </td>\r\n" + "              </tr>\r\n"
					+ "            </table>\r\n" + "          <![endif]-->\r\n" + "			</td>\r\n"
					+ "		</tr>\r\n" + "		<tr>\r\n" + "			<td>\r\n" + "				\r\n"
					+ "				<table class=\"content\" align=\"center\" cellpadding=\"0\"\r\n"
					+ "					cellspacing=\"0\" border=\"0\" bgcolor=\"#F7F7F7\"\r\n"
					+ "					style=\"width: 600px; max-width: 600px;\">\r\n" + "					<tr>\r\n"
					+ "						<td colspan=\"2\" style=\"background: #fff; border-radius: 8px;\">\r\n"
					+ "							<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
					+ "								<tr>\r\n" + "									<td\r\n"
					+ "										style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;\">\r\n"
					+ "								<tr class=\"\">\r\n"
					+ "									<td class=\"grid__col\"\r\n"
					+ "										style=\"font-family: 'Helvetica neue', Helvetica, arial, sans-serif; padding: 32px 40px;\">\r\n"
					+ "\r\n" + "										<h2\r\n"
					+ "											style=\"color: #404040; font-weight: 300; margin: 0 0 12px 0; font-size: 20px; line-height: 30px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\">\r\n"
					+ "\r\n" + "											Hi {" + bookPackage.getUser().getName()
					+ "},</h2>\r\n" + "\r\n" + "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 15px; line-height: 21px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\"\r\n"
					+ "											class=\"\">Your reservation request for {{"
					+ bookPackage.getPackage1().getPackageName() + "}},\r\n"

					+ "											your booking.</p>\r\n" + "\r\n"
					+ "										<table width=\"100%\" border=\"2\" cellspacing=\"0\" cellpadding=\"0\"\r\n"
					+ "											style=\"margin-top: 12px; margin-bottom: 12px; margin: 24px 0; color: #666666; font-weight: 400; font-size: 15px; line-height: 21px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\">\r\n"
					+ "											<tr>\r\n"
					+ "												<td\r\n"
					+ "													style=\"padding: 20px 20px 0px; font-weight: 700; font-size: 25px;\">\r\n"
					+ "													\r\n"
					+ "												<p\r\n"
					+ "														style=\"padding-top: -5px; font-weight: 700; font-size: 12px;\">Booking\r\n"
					+ "														Confirmation Code: {{"
					+ bookPackage.getOrderId() + "}}</p>\r\n" + "\r\n"
					+ "												</td>\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"

					+ "\r\n" + "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												<td></td>\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												\r\n"
					+ "											</tr>\r\n"
					+ "											<tr>\r\n"
					+ "												<td>\r\n" + "\r\n"
					+ "													<table style=\"width: 100%;!important\">\r\n"
					+ "\r\n" + "														\r\n"
					+ "														\r\n"
					+ "														\r\n" + "\r\n"
					+ "														\r\n"
					+ "														\r\n"
					+ "														<tr>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 20px; font-weight: 700; font-size: 14px; color: #000\">Grand\r\n"
					+ "																Total</td>\r\n"
					+ "															<td></td>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 30px; font-weight: 700; font-size: 14px; color: #000;\">₹\r\n"
					+ "																" + bookPackage.getBookingPrice()
					+ "</td>\r\n" + "														</tr>\r\n"
					+ "														<tr>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 20px; font-weight: 700; font-size: 14px;\">Payment\r\n"
					+ "																Mode</td>\r\n"
					+ "															<td></td>\r\n"
					+ "															<td\r\n"
					+ "																style=\"padding: 5px 20px 10px 30px; font-weight: 700; font-size: 14px;\">Pay\r\n"
					+ "																@ Online</td>\r\n"
					+ "														</tr>\r\n" + "\r\n" + "\r\n"
					+ "														</tr>\r\n" + "\r\n" + "\r\n"
					+ "													</table>\r\n" + "\r\n" + "\r\n"
					+ "												</td>\r\n"
					+ "											<tr>\r\n"
					+ "												<td\r\n"
					+ "													style=\"padding: 20px 20px 10px; font-weight: 700; font-size: 18px;\">Details\r\n"
					+ "													<p\r\n"
					+ "														style=\"padding-top: 0px; font-weight: 700; font-size: 12px;\">\r\n"
					+ "													<ul\r\n"
					+ "														style=\"padding-top: 0px; font-weight: 300; font-size: 14px;\">\r\n"
					+ "														<li>"
					+ bookPackage.getPackage1().getDescription() + "</li>\r\n"
					+ "														\r\n"
					+ "													</ul>\r\n"
					+ "													</p>\r\n"
					+ "												</td>\r\n" + "\r\n"
					+ "											</tr>\r\n" + "\r\n" + "\r\n"
					+ "											<td></td>\r\n" + "\r\n" + "\r\n"
					+ "											</tr>\r\n" + "\r\n"
					+ "											</tr>\r\n"
					+ "										</table> \r\n"
					+ "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 15px; line-height: 21px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif;\"\r\n"
					+ "											class=\"\">Hope you enjoyed the booking experience and will\r\n"
					+ "											like the stay too.</p>\r\n"
					+ "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 17px; line-height: 24px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif; margin-bottom: 6px; margin-top: 24px;\"\r\n"
					+ "											class=\"\">Cheers,</p>\r\n"
					+ "										<p\r\n"
					+ "											style=\"color: #666666; font-weight: 400; font-size: 17px; font-family: 'Helvetica neue', Helvetica, arial, sans-serif; margin-bottom: 6px; margin-top: 10px;\">Hotel\r\n"
					+ "											Direct Team</p>\r\n"
					+ "									</td>\r\n" + "								</tr>\r\n"
					+ "								</td>\r\n" + "								</tr>\r\n"
					+ "							</table>\r\n" + "						</td>\r\n"
					+ "					</tr>\r\n" + "				</table> \r\n" + "			</td>\r\n"
					+ "		</tr>\r\n" + "	</table>\r\n" + "</body>\r\n" + "\r\n" + "</html>";

			email.setMessage(html);

			emailService.sendEmail(email);

		}

		return ResponseEntity.ok(Map.of("msg", "updated"));

	}

	@PostMapping("/change")

	public String ChnagePasword(@RequestParam("old") String old, @RequestParam("new") String NewPassword,
			HttpSession Session, Model model) {

		User user = (User) model.getAttribute("user");

		String upass = user.getPassword();

		if (this.bCryptPasswordEncoder.matches(old, upass)) {

			user.setPassword(this.bCryptPasswordEncoder.encode(NewPassword));

			User saveUser = userService.SaveUser(user);

			Session.setAttribute("msg", "Password Change Successfully !!");

		} else {

			Session.setAttribute("msg", "Invalid Current Password Please Try Again !!");

			return "redirect:/user/index";

		}

		return "redirect:/user/index";

	}
	
	
	// Bus Search Handler
		@GetMapping("/bussearch/{query}")
		public ResponseEntity<?> Search(@PathVariable("query") String query){
			
			System.out.println("Query : "+query);

			List<Bus> searchingBus = busService.SearchingBus(query);
			System.out.println("sdkjfhfkad -> "+searchingBus);
			
			return ResponseEntity.ok(searchingBus);
		}

}
