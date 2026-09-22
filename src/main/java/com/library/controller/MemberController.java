package com.library.controller;

import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    // Constructor Injection (removes @Autowired yellow line)
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "member-list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }

    @PostMapping
    public String addMember(@ModelAttribute Member member) {
        memberRepository.save(member);
        return "redirect:/members";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable int id) {
        memberRepository.deleteById(id);
        return "redirect:/members";
    }
}