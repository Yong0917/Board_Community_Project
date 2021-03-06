package yong.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yong.board.service.MemberService;
import yong.board.vo.MemberVo;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member")  //세션값 없으면 redirect
    public String memberInfo(Model model, HttpSession session) {

       return "member";

    }

    @ResponseBody   //사용자 리스트
    @GetMapping(value = "/getMemberList.do")
    public List<MemberVo> getMemberList(Model model) {

        List<MemberVo>  list = memberService.selectMemberList();
        model.addAttribute("list",list);

        return list;
    }

    @ResponseBody   //사용자 정보
    @GetMapping(value = "/getUserInfo")
    public MemberVo getUserInfo(MemberVo memberVo) {
        return memberService.getUserInfo(memberVo.getId());
    }


    @ResponseBody   //비밀번호 수정
    @PostMapping(value = "/modifyUserPwd")
    public String modifyUserPwd(@Validated MemberVo memberVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {        //valid error
            return "Error";
        }

        return memberService.modifyUserPwd(memberVo);
    }


    @PostMapping("/deleteUser") //사용자 삭제
    public String deleteMember(MemberVo memberVo) {

        memberService.deleteUser(memberVo);

        return "redirect:/member";
    }


    @ResponseBody  //사용자 댓글 리스트
    @GetMapping(value = "/getCommentList.do")
    public List<MemberVo> getMemberList(Model model, MemberVo memberVo)  {

        String id = memberVo.getId();

        List<MemberVo> list = memberService.selectCommentList(id);
        model.addAttribute("list",list);

        return list;
    }

    //비밀번호 업데이트(주기별)
    @ResponseBody
    @GetMapping(value = "/pwSchedule.do")
    private String pwSchedule(MemberVo memberVo) {

        String id = memberVo.getId();

        String scheduleCheck = memberService.pwSchedule(id);


        return scheduleCheck;   //return Change || noChange

    }

}
