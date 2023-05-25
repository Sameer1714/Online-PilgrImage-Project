package com.mytrip.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mytrip.entities.Bus;
import com.mytrip.entities.BusBook;
import com.mytrip.entities.Flight;
import com.mytrip.entities.FlightBook;
import com.mytrip.entities.HolidayPackageBook;
import com.mytrip.entities.Hotel;
import com.mytrip.entities.HotelBook;
import com.mytrip.entities.Package;
import com.mytrip.entities.Train;
import com.mytrip.entities.TrainBook;
import com.mytrip.entities.User;
import com.mytrip.helper.Message;
import com.mytrip.service.BusBokkinService;
import com.mytrip.service.BusService;
import com.mytrip.service.FlightBookService;
import com.mytrip.service.FlightService;
import com.mytrip.service.HotelBookService;
import com.mytrip.service.HotelService;
import com.mytrip.service.PackageBookingService;
import com.mytrip.service.PackageService;
import com.mytrip.service.TrainBookingService;
import com.mytrip.service.TrainService;
import com.mytrip.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private FlightService flightService;

	@Autowired
	private TrainService trainService;

	@Autowired
	private BusService busService;
	
	@Autowired
	private BusBokkinService busBokkinService;
	
	@Autowired
	private FlightBookService flightBookService;
	
	@Autowired
	private TrainBookingService trainBookingService;
	
	@Autowired
	private HotelBookService hotelBookService;
	
	@Autowired
	private PackageBookingService packageBookingService;

	@Autowired
	private PackageService packageService;

	@ModelAttribute
	public void addcommanData(Model model, Principal principal) {
		String name = principal.getName();
		User user = userService.finduserByEmail(name);
		System.out.println(user);
		model.addAttribute("user", user);

		List<Hotel> listofhotel = hotelService.GetAllHotel();
		model.addAttribute("listofhotel", listofhotel);

		List<Flight> listofflight = flightService.getAllFlight();
		model.addAttribute("listofflight", listofflight);

		List<Train> listoftrain = trainService.GetAllTrain();
		model.addAttribute("listoftrain", listoftrain);

		List<Bus> listofbus = busService.GetAllBus();
		model.addAttribute("listofbus", listofbus);

	}

	@GetMapping("/index")
	public String AdminDashboard(Model model) {
		
		long getBusCount = busService.GetBusCount();
		model.addAttribute("getBusCount", getBusCount);
		
		long getBookingCount = busBokkinService.GetBookingCount();
		model.addAttribute("getBookingCount", getBookingCount);
		
		long trainCOunt = trainService.TrainCOunt();
		model.addAttribute("trainCOunt", trainCOunt);
		
		long trainBookinhCOunt = trainBookingService.TrainBookinhCOunt();
		model.addAttribute("trainBookinhCOunt", trainBookinhCOunt);
		
		long hotelCOunt = hotelService.HotelCOunt();
		model.addAttribute("hotelCOunt", hotelCOunt);
		
		long hotelBookingCOunt = hotelBookService.HotelBookingCOunt();
		model.addAttribute("hotelBookingCOunt", hotelBookingCOunt);
		
		long packageCount = packageService.PackageCount();
		model.addAttribute("packageCount", packageCount);
		
		long packageBookingCount = packageBookingService.PackageBookingCount();
		model.addAttribute("packageBookingCount", packageBookingCount);
		
		long flightCount = flightService.FlightCount();
		model.addAttribute("flightCount", flightCount);
		
		long flightBookingCount = flightBookService.FlightBookingCount();
		model.addAttribute("flightBookingCount", flightBookingCount);
		
		long userCount = userService.UserCount();
		model.addAttribute("userCount", userCount);

		model.addAttribute("title", "Admin Dashboard");

		return "admin/admindashboard";
	}

	// view

	@GetMapping("/viewpackage")
	public String ViewPackage(Model model) {

		model.addAttribute("title", "View-Package");

		List<Package> allPAckage = packageService.getAllPAckage();
		model.addAttribute("allPAckage", allPAckage);

		return "admin/viewpackage";
	}

	@GetMapping("/viewflight")
	public String ViewFlight(Model model) {

		model.addAttribute("title", "View-Flight");

		return "admin/viewflight";
	}

	@GetMapping("/viewhotel")
	public String ViewHotel(Model model) {

		model.addAttribute("title", "View-Hotels");

		return "admin/viewhotel";
	}

	@GetMapping("/viewbus")
	public String ViewBus(Model model) {

		model.addAttribute("title", "View-Bus");

		return "admin/viewbus";
	}

	@GetMapping("/viewtrain")
	public String ViewTrains(Model model) {

		model.addAttribute("title", "View-Trains");

		return "admin/viewtrain";
	}

	@GetMapping("/viewenquires")
	public String ViewEnquires(Model model) {

		model.addAttribute("title", "View-Enquires");

		return "admin/viewenquires";
	}

	// booking

	@GetMapping("/bookpackage")
	public String bookPackage(Model model) {
		
		List<HolidayPackageBook> getBookingHoliday = packageBookingService.GetBookingHoliday();
		model.addAttribute("getBookingHoliday", getBookingHoliday);

		model.addAttribute("title", "book-Package");

		return "admin/bookpackage";
	}

	@GetMapping("/bookflight")
	public String bookFlight(Model model) {
		
		List<FlightBook> allFlightBooking = flightBookService.getAllFlightBooking();
		model.addAttribute("allFlightBooking", allFlightBooking);

		model.addAttribute("title", "book-Flight");

		return "admin/bookflight";
	}

	@GetMapping("/bookhotel")
	public String bookHotel(Model model) {
		List<HotelBook> getAllBookHotel = hotelBookService.GetAllBookHotel();
		model.addAttribute("getAllBookHotel", getAllBookHotel);

		model.addAttribute("title", "book-Hotels");

		return "admin/bookhotel";
	}

	@GetMapping("/bookbus")
	public String bookBus(Model model) {
		
		List<BusBook> getAllBookingBusDetails = busBokkinService.GetAllBookingBusDetails();
		
		model.addAttribute("getAllBookingBusDetails",getAllBookingBusDetails);

		model.addAttribute("title", "book-Bus");

		return "admin/bookbus";
	}

	@GetMapping("/booktrain")
	public String bookTrains(Model model) {
		
		List<TrainBook> allBookingTrain = trainBookingService.getAllBookingTrain();
		model.addAttribute("allBookingTrain", allBookingTrain);

		model.addAttribute("title", "book-Trains");

		return "admin/booktrain";
	}

	@PostMapping("/addhotel")
	public String AddHotel(@ModelAttribute("hotel") Hotel hotel, @RequestParam("photo") MultipartFile file, Model model,
			HttpSession session) {

		try {

			hotel.setHotelImgUrl(file.getOriginalFilename());

			File savefile = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			Hotel hotel2 = hotelService.AddHotel(hotel);

			if (hotel2 != null) {

				session.setAttribute("message", new Message(" Hotel Added Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/index";
		}

		return "redirect:/admin/index";
	}

	@PostMapping("/addflight")
	public String AddFlight(@ModelAttribute("flight") Flight flight, HttpSession session, Model model) {

		try {

			Flight fligh = flightService.AddFligh(flight);

			if (fligh != null) {

				session.setAttribute("message", new Message(" Flight Added Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/index";
		}

		return "redirect:/admin/index";

	}

	@PostMapping("/addtrain")
	public String AddTrain(@ModelAttribute("train") Train train, HttpSession session, Model model) {

		try {

			Train addTrain = trainService.AddTrain(train);

			if (addTrain != null) {

				session.setAttribute("message", new Message(" Train Added Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/index";
		}

		return "redirect:/admin/index";

	}

	@PostMapping("/addbus")
	public String AddBus(@ModelAttribute("bus") Bus bus, HttpSession session, Model model) {

		try {

			Bus addBus = busService.AddBus(bus);

			if (addBus != null) {

				session.setAttribute("message", new Message(" Bus Added Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/index";
		}

		return "redirect:/admin/index";

	}

	@PostMapping("/addpackage")
	public String AddPacakge(@ModelAttribute("package1") Package package1,
			@RequestParam("packagephoto") MultipartFile file, Model model, HttpSession session) {

		try {

			package1.setPackageImgUrl(file.getOriginalFilename());

			File savefile = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			Package addPackage = packageService.AddPackage(package1);

			if (addPackage != null) {

				session.setAttribute("message", new Message(" Holiday Package Added Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/index";
		}

		return "redirect:/admin/index";
	}

	@GetMapping("/deletehotel/{hId}")
	public String deleteHotel(@PathVariable("hId") Integer hId, Model model, HttpSession session) {

		hotelService.deleteHotel(hId);

		return "redirect:/admin/viewhotel";

	}

	@GetMapping("/deleteflight/{fId}")
	public String deleteFlight(@PathVariable("fId") Integer fId, Model model, HttpSession session) {

		flightService.DeleteFlightById(fId);

		return "redirect:/admin/viewflight";

	}

	@GetMapping("/deletetrain/{tId}")
	public String deleteTrain(@PathVariable("tId") Integer tId, Model model, HttpSession session) {

		trainService.DeleteTrainById(tId);

		return "redirect:/admin/viewtrain";

	}

	@GetMapping("/deletebus/{bId}")
	public String deleteBus(@PathVariable("bId") Integer bId, Model model, HttpSession session) {

		busService.DeleteBusById(bId);

		return "redirect:/admin/viewbus";

	}

	@GetMapping("/deletepackage/{pId}")
	public String deletePackage(@PathVariable("pId") Integer pId, Model model, HttpSession session) {

		packageService.DeletePackageById(pId);

		return "redirect:/admin/viewpackage";

	}

	@PostMapping("/editbus")
	public String EditBus(@ModelAttribute("bus") Bus bus, HttpSession session, Model model) {

		try {

			Bus addBus = busService.AddBus(bus);

			if (addBus != null) {

				session.setAttribute("message", new Message(" Bus Edit Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/viewbus";
		}

		return "redirect:/admin/viewbus";

	}

	@PostMapping("/edittrain")
	public String EditTrain(@ModelAttribute("train") Train train, HttpSession session, Model model) {

		try {

			Train addTrain = trainService.AddTrain(train);

			if (addTrain != null) {

				session.setAttribute("message", new Message(" Train Edit Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/viewtrain";
		}

		return "redirect:/admin/viewtrain";

	}

	@PostMapping("/editpackage")
	public String EditPacakge(@ModelAttribute("package1") Package package1,
			@RequestParam("packagephoto") MultipartFile file, Model model, HttpSession session) {

		try {

			package1.setPackageImgUrl(file.getOriginalFilename());

			File savefile = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			Package addPackage = packageService.AddPackage(package1);

			if (addPackage != null) {

				session.setAttribute("message", new Message(" Holiday Package Edit Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/viewpackage";
		}

		return "redirect:/admin/viewpackage";
	}

	@PostMapping("/editflight")
	public String EditFlight(@ModelAttribute("flight") Flight flight, HttpSession session, Model model) {

		try {

			Flight fligh = flightService.AddFligh(flight);

			if (fligh != null) {

				session.setAttribute("message", new Message(" Flight Edit Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/viewflight";
		}

		return "redirect:/admin/viewflight";

	}

	@PostMapping("/edithotel")
	public String EditHotel(@ModelAttribute("hotel") Hotel hotel, @RequestParam("photo") MultipartFile file,
			Model model, HttpSession session) {

		try {

			hotel.setHotelImgUrl(file.getOriginalFilename());

			File savefile = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			Hotel hotel2 = hotelService.AddHotel(hotel);

			if (hotel2 != null) {

				session.setAttribute("message", new Message(" Hotel Edit Successfully !!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			}

		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Server Problem Try Again !!", "alert-danger"));
			return "redirect:/admin/viewhotel";
		}

		return "redirect:/admin/viewhotel";
	}
}
