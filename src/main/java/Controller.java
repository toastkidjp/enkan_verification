import enkan.data.HttpResponse;

/**
 * Controller.
 * @author Toast kid
 *
 */
public class Controller {

    public HttpResponse<String> index() {
        return HttpResponse.of("Hello!");
    }

}
