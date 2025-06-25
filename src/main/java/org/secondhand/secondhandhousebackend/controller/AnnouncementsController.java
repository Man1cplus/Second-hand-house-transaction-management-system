package org.secondhand.secondhandhousebackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.secondhand.secondhandhousebackend.entity.Announcements;
import org.secondhand.secondhandhousebackend.service.AnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announcement")
public class AnnouncementsController {

    @Autowired
    public AnnouncementsService announcementsService;

    // 创建新的公告
    @PostMapping
    public boolean createAnnouncement(@RequestBody Announcements announcement) {
        return announcementsService.save(announcement);
    }

    // 更新公告信息
    @PutMapping("/{announcementid}")
    public boolean updateAnnouncement(@PathVariable Integer announcementid, @RequestBody Announcements announcement) {
        announcement.setAnnouncementid(announcementid);
        return announcementsService.updateById(announcement);
    }

    // 删除公告
    @DeleteMapping("/{announcementid}")
    public boolean deleteAnnouncement(@PathVariable Integer announcementid) {
        return announcementsService.removeById(announcementid);
    }

    // 获取单个公告详情
    @GetMapping("/{announcementid}")
    public Announcements getAnnouncement(@PathVariable Integer announcementid) {
        return announcementsService.getById(announcementid);
    }

    // 获取所有公告
    @GetMapping
    public Page<Announcements> getAllAnnouncements(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return announcementsService.page(new Page<>(page, size));
    }
}
