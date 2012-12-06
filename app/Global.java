
import play.GlobalSettings;
import play.Logger;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import utils.Util;

import static play.mvc.Results.*;


public class Global extends GlobalSettings {

	@Override
	public Result onError(RequestHeader request, Throwable exception) {
		Logger.info("Error in processing request - " + request.toString(), exception);
		return ok(views.html.errorhandler.render(Util.HTTP_500));
	}

	@Override
	public Result onBadRequest(RequestHeader request, String error) {
		Logger.info("Bad Request - " + request.toString() + "\nError String - " + error);
		return ok(views.html.errorhandler.render(Util.HTTP_400));
	}

	@Override
	public Result onHandlerNotFound(RequestHeader request) {
		Logger.info("Handler Not Found - " + request.toString());
		return ok(views.html.errorhandler.render(Util.HTTP_404));
	}

}
