package br.com.techgol.app.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.techgol.app.model.TimeSheet;

public class TimeSheetUtils {

    public static Duration calcularTempoTotalPorDiaSemSobreposicao(List<TimeSheet> registros) {
        if (registros == null || registros.isEmpty()) return Duration.ZERO;

        // Agrupa por data (apenas o dia)
        Map<LocalDate, List<TimeSheet>> porDia = registros.stream()
                .filter(r -> r.getInicio() != null && r.getFim() != null)
                .collect(Collectors.groupingBy(r -> r.getInicio().toLocalDate()));

        Duration total = Duration.ZERO;

        for (Map.Entry<LocalDate, List<TimeSheet>> entry : porDia.entrySet()) {
            List<TimeSheet> diaRegistros = entry.getValue();

            // Converte em lista de intervalos
            List<Interval> intervalos = diaRegistros.stream()
                    .map(r -> new Interval(r.getInicio(), r.getFim()))
                    .sorted(Comparator.comparing(i -> i.start))
                    .collect(Collectors.toList());

            // Merge e soma
            total = total.plus(calcularDuracaoSemSobreposicao(intervalos));
        }

        return total;
    }

    private static Duration calcularDuracaoSemSobreposicao(List<Interval> intervalos) {
        if (intervalos.isEmpty()) return Duration.ZERO;

        List<Interval> merged = new ArrayList<>();
        Interval current = intervalos.get(0);

        for (int i = 1; i < intervalos.size(); i++) {
            Interval next = intervalos.get(i);
            if (!next.start.isAfter(current.end)) {
                current.end = current.end.isAfter(next.end) ? current.end : next.end;
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);

        // Soma as durações
        Duration total = Duration.ZERO;
        for (Interval i : merged) {
            total = total.plus(Duration.between(i.start, i.end));
        }

        return total;
    }

    // Classe auxiliar
    private static class Interval {
        LocalDateTime start;
        LocalDateTime end;

        Interval(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }
    }
}
