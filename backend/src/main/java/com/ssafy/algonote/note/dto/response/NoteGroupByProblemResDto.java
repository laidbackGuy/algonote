package com.ssafy.algonote.note.dto.response;

import com.ssafy.algonote.problem.domain.Problem;
import com.ssafy.algonote.problem.dto.response.ProblemResDto;

import java.util.List;

public record NoteGroupByProblemResDto(
        int noteCount,
        ProblemResDto problem,
        List<NoteWithoutContentResDto> notes
) {
    public static NoteGroupByProblemResDto from(Problem problem, List<NoteWithoutContentResDto> notes) {
        return new NoteGroupByProblemResDto(notes.size(), ProblemResDto.from(problem), notes);
    }
}
