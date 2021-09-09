package com.hiyoon.webclientexample.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithItem {
    private UserVo userVo;
    private RepoVo repoVo;
    private List<UserVo> userVoList;
    private List<RepoVo> repoVoList;

    public UserWithItem(UserVo userVo, RepoVo repoVo) {
        this.userVo = userVo;
        this.repoVo = repoVo;
    }

    public UserWithItem(List<UserVo> userVoList, List<RepoVo> repoVoList) {
        this.userVoList = userVoList;
        this.repoVoList = repoVoList;
    }
}
