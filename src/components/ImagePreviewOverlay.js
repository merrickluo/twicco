import React from 'react'

import { View, Image, TouchableWithoutFeedback } from 'react-native'

export default class ImagePreviewOverlay extends React.Component {
  render() {
    if (!this.props.uri) return null

    return (
      <View style={styles.container} >
        <TouchableWithoutFeedback onPress={this.props.onClose}>
          <Image
            source={{ uri: this.props.uri }}
            style={styles.image}
          />
        </TouchableWithoutFeedback>
      </View>
    )
  }
}

const styles = {
  container: {
    position: 'absolute',
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: 'transparent',
  },
  image: {
    flex: 1,
    marginTop: 48,
    marginLeft: 24,
    marginRight: 24,
    marginBottom: 48,
  }
}
