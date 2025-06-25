package org.secondhand.secondhandhousebackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.secondhand.secondhandhousebackend.DTO.Result;
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
    public Result createAnnouncement(@RequestBody Announcements announcement) {
        boolean success = announcementsService.save(announcement);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to create announcement");
        }
    }

    // 更新公告信息
    @PutMapping("/{announcementid}")
    public Result updateAnnouncement(@PathVariable Integer announcementid, @RequestBody Announcements announcement) {
        announcement.setAnnouncementid(announcementid);
        boolean success = announcementsService.updateById(announcement);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to update announcement");
        }
    }

    // 删除公告
    @DeleteMapping("/{announcementid}")
    public Result deleteAnnouncement(@PathVariable Integer announcementid) {
        boolean success = announcementsService.removeById(announcementid);
        if (success) {
            return Result.ok();
        } else {
            return Result.fail("Failed to delete announcement");
        }
    }

    // 获取单个公告详情
    @GetMapping("/{announcementid}")
    public Result getAnnouncement(@PathVariable Integer announcementid) {
        Announcements announcement = announcementsService.getById(announcementid);
        if (announcement != null) {
            return Result.ok(announcement);
        } else {
            return Result.fail("Announcement not found");
        }
    }

    // 获取所有公告
    @GetMapping
    public Result getAllAnnouncements(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Page<Announcements> announcementsPage = announcementsService.page(new Page<>(page, size));
        return Result.ok(announcementsPage.getRecords(), announcementsPage.getTotal());
    }
}