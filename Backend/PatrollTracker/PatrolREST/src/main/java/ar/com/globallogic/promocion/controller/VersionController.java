package ar.com.globallogic.promocion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class VersionController {

	private static final String VERSION = "v 1.0";

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String home() {
		return VERSION;
	}

}