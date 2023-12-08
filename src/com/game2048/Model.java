package com.game2048;

import java.util.LinkedList;
import java.util.List;

public class Model {
  private static final int FIELD_WIDTH = 4;
  private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
  int maxTile = 0;
  int score = 0;

  public Model() {
    resetGameTiles();
  }

  Tile[][] getGameTiles() {
    return gameTiles;
  }

  void resetGameTiles() {
    for (int i = 0; i < FIELD_WIDTH; i++) {
      for (int j = 0; j < FIELD_WIDTH; j++) {
        gameTiles[i][j] = new Tile();
      }
    }
    addTile();
    addTile();
  }

  private void addTile() {
    List<Tile> emptyTiles = getEmptyTiles();
    if (emptyTiles.isEmpty())
      return;
    int index = (int) (Math.random() * emptyTiles.size()) % emptyTiles.size();
    Tile emptyTile = emptyTiles.get(index);
    emptyTile.value = Math.random() < 0.9 ? 2 : 4;
  }

  private List<Tile> getEmptyTiles() {
    List<Tile> emptyTiles = new java.util.ArrayList<>();
    for (int i = 0; i < FIELD_WIDTH; i++) {
      for (int j = 0; j < FIELD_WIDTH; j++) {
        if (gameTiles[i][j].isEmpty())
          emptyTiles.add(gameTiles[i][j]);
      }
    }
    return emptyTiles;
  }

  private boolean consolidateTiles(Tile[] tiles) {
    int insertPosition = 0;
    boolean result = false;
    for (int i = 0; i < FIELD_WIDTH; i++) {
      if (!tiles[i].isEmpty()) {
        if (i != insertPosition) {
          tiles[insertPosition] = tiles[i];
          tiles[i] = new Tile();
          result = true;
        }
        insertPosition++;
      }
    }
    return result;
  }

  private boolean mergeTiles(Tile[] tiles) {
    boolean result = false;
    LinkedList<Tile> tilesList = new LinkedList<>();
    for (int i = 0; i < FIELD_WIDTH; i++) {
      if (tiles[i].isEmpty()) {
        continue;
      }

      if (i < FIELD_WIDTH - 1 && tiles[i].value == tiles[i + 1].value) {
        int updatedValue = tiles[i].value * 2;
        if (updatedValue > maxTile) {
          maxTile = updatedValue;
        }
        score += updatedValue;
        tilesList.addLast(new Tile(updatedValue));
        tiles[i + 1].value = 0;
        result = true;
      } else {
        tilesList.addLast(new Tile(tiles[i].value));
      }
      tiles[i].value = 0;
    }

    for (int i = 0; i < tilesList.size(); i++) {
      tiles[i] = tilesList.get(i);
    }

    return result;
  }

  private Tile[][] rotateClockwise(Tile[][] tiles) {
    final int N = tiles.length;
    Tile[][] result = new Tile[N][N];
    for (int r = 0; r < N; r++) {
      for (int c = 0; c < N; c++) {
        result[c][N - 1 - r] = tiles[r][c];
      }
    }
    return result;
  }

  public void left() {
    boolean moveFlag = false;
    for (int i = 0; i < FIELD_WIDTH; i++) {
      if (consolidateTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
        moveFlag = true;
      }
    }
    if (moveFlag) {
      addTile();
    }
  }

  public void right() {
    gameTiles = rotateClockwise(gameTiles);
    gameTiles = rotateClockwise(gameTiles);
    left();
    gameTiles = rotateClockwise(gameTiles);
    gameTiles = rotateClockwise(gameTiles);
  }

  public void up() {
    gameTiles = rotateClockwise(gameTiles);
    gameTiles = rotateClockwise(gameTiles);
    gameTiles = rotateClockwise(gameTiles);
    left();
    gameTiles = rotateClockwise(gameTiles);
  }

  public void down() {
    gameTiles = rotateClockwise(gameTiles);
    left();
    gameTiles = rotateClockwise(gameTiles);
    gameTiles = rotateClockwise(gameTiles);
    gameTiles = rotateClockwise(gameTiles);
  }

  private int getEmptyTilesCount() {
    return getEmptyTiles().size();
  }

  private boolean isFull() {
    return getEmptyTilesCount() == 0;
  }

  boolean canMove() {
    if (!isFull()) {
      return true;
    }

    for (int x = 0; x < FIELD_WIDTH; x++) {
      for (int y = 0; y < FIELD_WIDTH; y++) {
        Tile t = gameTiles[x][y];
        if ((x < FIELD_WIDTH - 1 && t.value == gameTiles[x + 1][y].value)
            || ((y < FIELD_WIDTH - 1) && t.value == gameTiles[x][y + 1].value)) {
          return true;
        }
      }
    }
    return false;
  }
}
