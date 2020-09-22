package by.dobrush.swruneoptimizer.util;

import by.dobrush.swruneoptimizer.beans.Monster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OptimizeEntity {
    private Filter filter;
    private Monster monster;
}
