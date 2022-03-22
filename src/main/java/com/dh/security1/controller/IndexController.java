package com.dh.security1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dh.security1.config.auth.PrincipalDetails;
import com.dh.security1.config.youtube.AddSubscription;
import com.dh.security1.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping({"","/"})
	public String index() {
		return "index";
	}
	
	@RequestMapping("/subscribe")
	public ModelAndView subscribe(ModelMap model, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
//		youtube.cmdline.data.AddSubscription s = new youtube.cmdline.data.AddSubscription();
		System.out.println("=============================================== subscribe Api() ============================");
		String channel = request.getParameter("channel");
		System.out.println("channel : " + channel);
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		String username = principalDetails.getUsername();
		String providerId = userRepository.findUserByUsername(username).getProviderId();
		AddSubscription s = new AddSubscription(providerId);
		String result = s.start(channel);
		model.addAttribute("result", result);
		return new ModelAndView("redirect:subscriptionSuccess", model);
	}
	
	@GetMapping("loginSuccess")
	public String loginSuccess() { 
		return "loginSuccessPage";
	}
	
	@GetMapping("subscriptionSuccess")
	public String subscriptionSuccess(Model model, HttpServletRequest request) {
		String result = request.getParameter("result");
		model.addAttribute("result", result);
		return "subscriptionSuccessPage";
	}
	
	
}
