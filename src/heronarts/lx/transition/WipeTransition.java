/**
 * Copyright 2013- Mark C. Slee, Heron Arts LLC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package heronarts.lx.transition;

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;

public class WipeTransition extends LXTransition {

  public enum Direction {
    RIGHT, LEFT, DOWN, UP
  };

  private final Direction direction;
  private double depth;

  public WipeTransition(LX lx) {
    this(lx, Direction.RIGHT);
  }

  public WipeTransition(LX lx, Direction direction) {
    super(lx);
    this.direction = direction;
    this.depth = 5.;
  }

  public WipeTransition setDepth(double depth) {
    this.depth = depth;
    return this;
  }

  @Override
  @SuppressWarnings("fallthrough")
  protected void computeBlend(int[] c1, int[] c2, double progress) {
    double blendPosition = 0;
    switch (this.direction) {
    case RIGHT:
      blendPosition = -this.depth + (this.lx.width + this.depth * 2.)
          * progress;
      break;
    case LEFT:
      blendPosition = -this.depth + (this.lx.width + this.depth * 2.)
          * (1. - progress);
      break;
    case DOWN:
      blendPosition = -this.depth + (this.lx.height + this.depth * 2.)
          * progress;
      break;
    case UP:
      blendPosition = -this.depth + (this.lx.height + this.depth * 2.)
          * (1. - progress);
      break;
    }

    for (int i = 0; i < this.lx.total; ++i) {
      double nodePosition = 0;
      double distanceSign = 1;
      switch (this.direction) {
      case LEFT:
        distanceSign = -1;
      case RIGHT:
        nodePosition = this.lx.column(i);
        break;
      case UP:
        distanceSign = -1;
      case DOWN:
        nodePosition = this.lx.row(i);
        break;
      }
      double distance = (blendPosition - nodePosition) / (this.depth / 2.)
          * distanceSign;
      if (distance <= -1.) {
        this.colors[i] = c1[i];
      } else if (distance >= 1.) {
        this.colors[i] = c2[i];
      } else {
        this.colors[i] = LXColor.lerp(c1[i], c2[i], (distance + 1.) / 2.);
      }
    }
  }
}
