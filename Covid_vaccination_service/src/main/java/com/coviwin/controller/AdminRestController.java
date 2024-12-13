package com.coviwin.controller;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;

import org.aspectj.util.LangUtil.StringChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.coviwin.exception.AdminException;
import com.coviwin.model.Admin;
import com.coviwin.service.AdminLoginService;
import com.coviwin.service.AdminService;
import com.coviwin.exception.ApppintmentException;
import com.coviwin.exception.IdCardException;
import com.coviwin.exception.LoginException;
import com.coviwin.exception.MemberException;
import com.coviwin.exception.VaccinationCenterException;
import com.coviwin.exception.VaccineException;
import com.coviwin.exception.VaccineInventoryException;
import com.coviwin.model.Appointment;
//import com.coviwin.exception.VaccineInventoryException;


import com.coviwin.model.IdCard;
import com.coviwin.model.Member;
import com.coviwin.model.VaccinationCenter;
import com.coviwin.model.Vaccine;
import com.coviwin.model.VaccineCount;
import com.coviwin.model.VaccineInventory;
import com.coviwin.service.AppointmentService;
import com.coviwin.service.IdCardService;
import com.coviwin.service.MemberService;
import com.coviwin.service.VaccinationCenterService;
import com.coviwin.service.VaccineInventoryService;
import com.coviwin.service.VaccineService;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

	@Autowired
	public VaccinationCenterService vaccinationCenter;

	@Autowired
	public IdCardService idCardService;

	@Autowired
	public VaccineInventoryService vaccineInventoryService;

	@Autowired
	public VaccineService vService;

	@Autowired
	public MemberService memberService;

	@Autowired
	public AppointmentService appointmentService;

	@Autowired
	private AdminService cService;

	@Autowired
	private AdminLoginService adminLoginService;

//	Vaccination center Service method

	@GetMapping(value = "/centers")
	public ResponseEntity<List<VaccinationCenter>> allVaccinationCenter(@RequestParam String key) throws VaccinationCenterException, LoginException{
		adminLoginService.authenthicate(key);
		List<VaccinationCenter> vcLS = vaccinationCenter.getAllVaccineCenters();

		return new ResponseEntity<List<VaccinationCenter>>( vcLS , HttpStatus.FOUND );

	}

	@PostMapping("/admins")
	public ResponseEntity<Admin> saveAdmin(@RequestBody Admin customer) throws AdminException, LoginException{

		Admin savedCustomer=  cService.registerCustomer(customer);

		return new  ResponseEntity<Admin>(savedCustomer,HttpStatus.CREATED);
	}

	@PutMapping("/admins")
	public ResponseEntity<Admin> updateAdmin(@RequestParam String key, @RequestBody Admin customer) throws AdminException ,LoginException{
	 	adminLoginService.authenthicate(key);
	 Admin updateCustomer=	cService.updateCustomer(customer, key);
	 return new ResponseEntity<Admin>(updateCustomer,HttpStatus.OK);
	}



	@GetMapping(value="/center")
	public ResponseEntity<VaccinationCenter> getVaccineCenter(@RequestParam String key, @RequestParam Integer id) throws VaccinationCenterException ,LoginException{
			adminLoginService.authenthicate(key);
		VaccinationCenter vcLS = vaccinationCenter.getVaccineCenters(id);
		return  new ResponseEntity<VaccinationCenter>( vcLS , HttpStatus.FOUND );

	}

	@PostMapping(value="/center")
	public ResponseEntity< VaccinationCenter> addVaccineCenter(@RequestParam String key , @Valid @RequestBody VaccinationCenter vaccinationCenter ) throws VaccinationCenterException, LoginException{

			adminLoginService.authenthicate(key);
		VaccinationCenter vCenter = this.vaccinationCenter.addVaccineCenter(vaccinationCenter);

		return new ResponseEntity<>(vCenter , HttpStatus.CREATED);

	}

	@PutMapping(value = "/center")
	public ResponseEntity< VaccinationCenter > updateCenter(@RequestParam String key, @Valid @RequestBody VaccinationCenter vc ) throws VaccinationCenterException, LoginException {
			adminLoginService.authenthicate(key);
		VaccinationCenter retvc =  vaccinationCenter.updateVaccineCenter(vc);

		return new ResponseEntity<VaccinationCenter>( retvc , HttpStatus.ACCEPTED );

	}

	@DeleteMapping(value = "/center")
	public ResponseEntity<VaccinationCenter> deleteCenter(@RequestParam String key, @Valid @RequestBody VaccinationCenter center ) throws VaccinationCenterException, LoginException{
			adminLoginService.authenthicate(key);
		VaccinationCenter vCenter =  vaccinationCenter.deleteVaccineCenter( center );

		return new ResponseEntity<VaccinationCenter>( vCenter , HttpStatus.ACCEPTED );
	}

//	Id Card Service method

	@GetMapping("/getIdByAdhar")
	public ResponseEntity< List<IdCard> > getIdCardByAdhar(@RequestParam String key ,@Valid
													  @Digits(integer = 12,fraction = 12,message = "Length must be 12")
													  @RequestParam(value = "adhar") Long adharLong ) throws IdCardException, LoginException{
			adminLoginService.authenthicate(key);
		List<IdCard> idCard = idCardService.getAdharCardByNo(adharLong);

		return new ResponseEntity<>( idCard ,  HttpStatus.FOUND );
	}

	@GetMapping("/getIdByPan")
	public ResponseEntity< IdCard > getIdCardByPan(@RequestParam String key ,@RequestParam String pan ) throws IdCardException, LoginException{
			adminLoginService.authenthicate(key);
	 	IdCard idCard = idCardService.getPanCardByNumber( pan );

		return new ResponseEntity<>( idCard ,  HttpStatus.FOUND );
	}

	@PostMapping("/addId")
	public ResponseEntity< IdCard > addId(@RequestParam String key ,@Valid @RequestBody IdCard id ) throws IdCardException, LoginException{
			adminLoginService.authenthicate(key);
	 	IdCard idCard = idCardService.addIdCard( id );

		return new ResponseEntity<>( idCard ,  HttpStatus.FOUND );

	}

//	Vaccine Inventory Service Method

	@GetMapping("/vaccineInventories")
	public ResponseEntity<List< VaccineInventory >> getAllVacciceInventory() throws VaccineInventoryException {

		List< VaccineInventory > vInventory =  vaccineInventoryService.allVaccineInventory();

		return new ResponseEntity<List<VaccineInventory>>( vInventory , HttpStatus.FOUND );
	}

	@GetMapping("/vaccineInventories/vaccine")

	public ResponseEntity< List< VaccineInventory > > getVaccineInventoryByVaccine(@RequestParam String key, @Valid @RequestBody Vaccine vaccine ) throws VaccineInventoryException, LoginException {
		adminLoginService.authenthicate(key);

		List<VaccineInventory> vi = vaccineInventoryService.getVaccineInventoryByVaccine(vaccine);
        return new ResponseEntity<List<VaccineInventory>>(vi, HttpStatus.FOUND);
	}



	@DeleteMapping("/vaccineInventory/vi")
	public ResponseEntity<Boolean> deleteVaccineInventory(@RequestParam String key, @PathVariable("vi") Integer arg ) throws VaccineInventoryException, LoginException{
		adminLoginService.authenthicate(key);
		Boolean fBoolean =  vaccineInventoryService.deleteVaccineInventory( arg );

		return new ResponseEntity<Boolean>( fBoolean ,HttpStatus.ACCEPTED );
	}

	@PostMapping("/vaccineInventory/{centerId}")
	public ResponseEntity<VaccineInventory> addInventoryByCenter(@RequestParam String key ,@Valid @RequestBody VaccineInventory vInventory, @PathVariable("centerId") Integer centerId) throws VaccineInventoryException, LoginException {
		adminLoginService.authenthicate(key);
		VaccineInventory vaccineInventory =  vaccineInventoryService.addVaccineInventoryByCenter( centerId , vInventory);


		return new ResponseEntity<VaccineInventory>( vaccineInventory , HttpStatus.ACCEPTED );
	}

	@PostMapping("/vaccineInventory")
	public ResponseEntity< VaccineInventory > addVaccineCount(@RequestParam String key, @RequestParam("v") Integer vacid,
																  @RequestParam("vi") Integer vi,
																  @RequestBody VaccineCount vc )
																		  throws VaccineInventoryException, VaccineException, LoginException{
		adminLoginService.authenthicate(key);
		VaccineInventory vInventory = vaccineInventoryService.addVaccineCount( vacid, vi , vc );


		return new ResponseEntity<VaccineInventory>( vInventory , HttpStatus.ACCEPTED );

	}
	@PutMapping("/vaccineInventory")
	public ResponseEntity< VaccineInventory > updateVaccineInventory(@RequestParam String key ,@Valid @RequestParam Integer id, @RequestBody VaccineInventory vi ) throws VaccineInventoryException, LoginException{
		adminLoginService.authenthicate(key);
		VaccineInventory vaccineInventory = vaccineInventoryService.updateVaccineInventory(id, vi);

		return new ResponseEntity<VaccineInventory>( vaccineInventory , HttpStatus.ACCEPTED );
	}



//	Vaccine Service Method

	@PostMapping("/vaccine")
	public ResponseEntity<Vaccine> addVaccine(@RequestParam String key, @Valid @RequestBody Vaccine vaccine) throws VaccineException ,LoginException{
		adminLoginService.authenthicate(key);
		 Vaccine v = vService.addVaccine(vaccine);

		 return new ResponseEntity<>( v , HttpStatus.CREATED );

	}

	@GetMapping("/vaccines")

	public ResponseEntity< List< Vaccine >> getAllVaccine() throws VaccineException {
		List< Vaccine > vaccine =  vService.allVaccine();
		return new ResponseEntity< List<Vaccine> >( vaccine ,  HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/vaccine")
	public ResponseEntity< Boolean > deleteVaccine(@RequestParam String key, @Valid @RequestBody Vaccine v ) throws VaccineException ,LoginException{
		adminLoginService.authenthicate(key);
		Boolean boolean1 = vService.deleteVaccine( v );


		return new ResponseEntity<Boolean>( boolean1 , HttpStatus.ACCEPTED );
	}

	@GetMapping("/vaccine/{vaccine}")
	public ResponseEntity< Vaccine > getVaccineById(@RequestParam String key ,@PathVariable("vaccine") Integer id ) throws VaccineException ,LoginException{
		adminLoginService.authenthicate(key);
		Vaccine vaccine =  vService.getVaccineById( id );

		return new ResponseEntity<Vaccine>( vaccine  , HttpStatus.FOUND );
	}

	@GetMapping("/vaccine")
	public ResponseEntity< List< Vaccine > > getVaccineByName(@RequestParam String key ,@RequestParam String name) throws VaccineException ,LoginException{
		adminLoginService.authenthicate(key);
		List< Vaccine > vaccine = vService.getVaccineByName(name);

		return new ResponseEntity<List< Vaccine >>( vaccine , HttpStatus.ACCEPTED);
	}


//	Member Service

	@GetMapping( "/memberById" )
	public ResponseEntity< Member > getMemberById(@RequestParam String key, @RequestParam Integer id ) throws MemberException, IdCardException ,LoginException{
		adminLoginService.authenthicate(key);

		Member member = memberService.getMemberbyId(id);
		return new ResponseEntity<Member>( member , HttpStatus.FOUND );
	}

//

	@PostMapping("/member")
	public ResponseEntity< Member > addMember(@RequestParam String key, @RequestParam Integer id,@Valid @RequestBody  Member member ) throws MemberException, IdCardException ,LoginException{
		adminLoginService.authenthicate(key);
		Member member2 = memberService.addMember( id, member);

		return new  ResponseEntity<>( member2 , HttpStatus.ACCEPTED );

	}

	@DeleteMapping("/member")
	public ResponseEntity< Boolean > deleteMember(@RequestParam String key ,@Valid @RequestBody Member member ) throws MemberException ,LoginException{
		adminLoginService.authenthicate(key);
		Boolean member2 =  memberService.deleteMember(member);

		return new ResponseEntity<Boolean>( member2 , HttpStatus.ACCEPTED );
	}


	@GetMapping("/member")
	public ResponseEntity< Member > getMemberByPanNo(@RequestParam String key ,@RequestParam  String p ) throws MemberException, IdCardException ,LoginException{
			adminLoginService.authenthicate(key);
		Member member = memberService.getMemberByPanNo(p);

		return new ResponseEntity<Member>( member , HttpStatus.ACCEPTED );
	}

	@PutMapping("/member")
	public ResponseEntity< Member > updateMember(@RequestParam String key, @Valid @RequestBody Member member) throws MemberException ,LoginException{
			adminLoginService.authenthicate(key);
		Member member2 = memberService.updateMember(member);
		return new ResponseEntity<Member>( member2 , HttpStatus.ACCEPTED );
	}

	@GetMapping( "/memberByAdhar" )
	public ResponseEntity< Member > getMemberByAdhar(@RequestParam String key ,@RequestParam Long a ) throws MemberException, IdCardException ,LoginException{
			adminLoginService.authenthicate(key);
		Member member = memberService.getMemberByAdharNo(a);

		return new ResponseEntity<Member>( member , HttpStatus.FOUND );
	}

//	AppointmentService Method

	@GetMapping("/appointments")
	public ResponseEntity< List<Appointment>> getAllAppointment() throws ApppintmentException{

		List<Appointment> ls = appointmentService.allAppointment();
		return new ResponseEntity<List<Appointment>>( ls , HttpStatus.FOUND );
	}

	@DeleteMapping("/appointment")
	public ResponseEntity<Boolean> deleteAppointment(@RequestParam String key, @Valid @RequestBody Appointment appointment ) throws ApppintmentException ,LoginException{
			adminLoginService.authenthicate(key);
		Boolean appointment2 =  appointmentService.deleteAppointment(appointment);
		return new ResponseEntity<Boolean>(appointment2 , HttpStatus.ACCEPTED);
	}

	@GetMapping("/appointment")
	public ResponseEntity< Appointment > getAppointment (@RequestParam String key, @RequestParam Long bid ) throws ApppintmentException ,LoginException{
			adminLoginService.authenthicate(key);
		Appointment appointment =  appointmentService.getAppointment(bid);

		return new ResponseEntity<Appointment>(appointment,HttpStatus.FOUND);
	}

	@PutMapping("appointment")
	public ResponseEntity<Appointment> updateAppointment(@RequestParam String key, @Valid @RequestBody Appointment appointment ) throws ApppintmentException ,LoginException{
			adminLoginService.authenthicate(key);
		Appointment app = appointmentService.updateAppointment(appointment);
		return new ResponseEntity< Appointment>(app,HttpStatus.ACCEPTED);
	}


}
	




