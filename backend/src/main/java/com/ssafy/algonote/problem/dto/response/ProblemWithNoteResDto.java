package com.ssafy.algonote.problem.dto.response;

import com.ssafy.algonote.note.dto.response.NoteGroupByProblemResDto;

import java.util.List;

public record ProblemWithNoteResDto(
        int problemCount,
        List<NoteGroupByProblemResDto> problems
) {
    public static ProblemWithNoteResDto from(List<NoteGroupByProblemResDto> problems) {
        return new ProblemWithNoteResDto(
                problems.size(),
                problems
        );
    }

}
