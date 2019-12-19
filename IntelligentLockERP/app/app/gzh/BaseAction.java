package app.gzh;

import app.gzh.BaseAction;
import play.Logger;
import play.Logger.ALogger;
import play.mvc.Controller;

public class BaseAction extends Controller{
	protected static ALogger appLogger = Logger.of(BaseAction.class);
}
