package de.anmimi.news.controller;

import de.anmimi.news.headlines.HeadlineSummaryResult;
import de.anmimi.news.headlines.data.Headline;
import de.anmimi.news.headlines.CompareService;
import de.anmimi.news.headlines.HeadlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final HeadlineService headlineService;
    private final CompareService compareService;

    @GetMapping
    public List<Headline> headlinesOfLastThreeHours() {
        return headlineService.getNewestHeadlines();
    }

    @GetMapping("/all")
    public List<Headline> allHeadLines(@RequestParam(required = false) Integer pastHours) {
        if (pastHours != null) {
            return headlineService.findHeadLinesOfLast(pastHours);
        }
        return headlineService.getAllHeadlines();
    }

    @PostMapping("/{id}/compare")
    public List<HeadlineSummaryResult> compareHeadline(@PathVariable String id) {
        return compareService.searchForSimiliar(id);
    }

}
