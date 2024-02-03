package cn.wolfcode.wolf2w.search.controller;

import cn.wolfcode.wolf2w.redis.core.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RequestMapping("/q")
public class SearchController {
    public R<?> search(Integer type,String keyword){
        try {
            keyword = URLDecoder.decode(keyword, "UTF-8");
            switch (type){
                case 0:
                    return this.searchForDest(keyword);
                case 1:
                    return this.searchForStrategy(keyword);
                case 2:
                    return this.searchForTravel(keyword);
                case 3:
                    return this.searchForUser(keyword);
                default:
                    return this.searchForAll(keyword);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return R.ok();
    }

    private R<?> searchForAll(String keyword) {
        return null;
    }

    private R<?> searchForUser(String keyword) {
        return null;
    }

    private R<?> searchForTravel(String keyword) {
        return null;
    }

    private R<?> searchForStrategy(String keyword) {
        return null;
    }

    private R<?> searchForDest(String keyword) {
        return null;
    }
}
