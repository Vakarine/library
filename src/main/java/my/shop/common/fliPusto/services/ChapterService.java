package my.shop.common.fliPusto.services;

import my.shop.common.fliPusto.origins.Chapter;
import my.shop.common.fliPusto.repositories.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ChapterService {
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    private void bookInit(Chapter chapter) {
        chapter.getBook();
    }

    public Chapter findChapterById(Long chapterId, boolean initBook) {
        Optional<Chapter> chapterOptional = chapterRepository.findById(chapterId);

        if (chapterOptional.isPresent()) {
            Chapter chapter = chapterOptional.get();

            if (initBook) {
                bookInit(chapter);
            }

            return chapter;
        }

        return null;
    }

    @Transactional
    public boolean updateChapter(Chapter chapter, Long originalId) {
        Chapter chapterOriginal = findChapterById(originalId, true);

        if (chapterOriginal == null) {
            return false;
        }

        chapterOriginal.setContent(chapter.getContent());
        chapterOriginal.setName(chapter.getName());

        chapterRepository.save(chapterOriginal);
        return true;
    }
}
