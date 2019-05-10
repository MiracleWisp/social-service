package social.feign;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import social.dto.TrackListDTO;

public interface MusicClient {
    @RequestMapping(method = RequestMethod.POST, value = "/track_list/create")
    TrackListDTO createTrackList(@RequestParam("name") String name);

    @RequestMapping(method = RequestMethod.POST, value = "/track_list/user")
    void addUserToTrackList(@RequestParam("track_list_id") String trackListId, @RequestParam("username") String username);

    @RequestMapping(method = RequestMethod.DELETE, value = "/track_list/user")
    void removeUserToTrackList(@RequestParam("track_list_id") String trackListId, @RequestParam("username") String username);
}